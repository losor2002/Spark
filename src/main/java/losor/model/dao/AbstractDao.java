package losor.model.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import losor.model.bean.Bean;
import losor.model.util.BeanUtil;
import losor.model.util.DbConnection;

public abstract class AbstractDao<T extends Bean> implements Dao<T>
{
    protected final String tableName;
    protected final List<Field> id, attributes;
    private final Class<T> beanClass;
    private final String select, update, insert, delete, exists;

    protected AbstractDao(Class<T> beanClass)
    {
        if(beanClass == null)
            throw new NullPointerException();

        this.beanClass = beanClass;
        Table table = beanClass.getDeclaredAnnotation(Table.class);
        if(table == null)
            throw new IllegalArgumentException("Il Bean non ha l'annotazione Table");
        tableName = table.name();
        if(tableName.equals(""))
            throw new IllegalArgumentException("L'annotazione Table non ha il nome");

        List<Field> id = new ArrayList<>(), attributes = new ArrayList<>();
        Field[] fields = beanClass.getDeclaredFields();
        for(int i = 0; i < fields.length; i++)
        {
            Field field = fields[i];
            Column column = field.getAnnotation(Column.class);
            if(field.getAnnotation(Id.class) != null)
            {
                if(column == null)
                    throw new IllegalArgumentException("L'Id non ha l'annotazione Column");
                if(column.name().equals(""))
                    throw new IllegalArgumentException("L'Id ha un'annotazione Column senza nome");
                id.add(field);
            }
            else if(column != null)
            {
                if(column.name().equals(""))
                    throw new IllegalArgumentException("L'attributo ha un'annotazione Column senza nome");
                attributes.add(field);
            }
        }
        if(id.size() == 0)
            throw new IllegalArgumentException("Il bean non ha un Id");
        this.id = List.copyOf(id);
        this.attributes = List.copyOf(attributes);

        select = "SELECT *\n"
              +  "FROM " + tableName;

        StringBuilder update = new StringBuilder("UPDATE " + tableName + "\n"
                                             +   "SET");
        for(Field f: this.id)
            update.append(" " + f.getAnnotation(Column.class).name() + "=?,");
        for(Field f: this.attributes)
            update.append(" " + f.getAnnotation(Column.class).name() + "=?,");
        update.delete(update.length() - 1, update.length());
        update.append("\nWHERE");
        for(Field f: this.id)
            update.append(" " + f.getAnnotation(Column.class).name() + "=? and");
        update.delete(update.length() - 4, update.length());
        this.update = new String(update);

        StringBuilder insert = new StringBuilder("INSERT INTO " + tableName + "(");
        int count = 0;
        for(Field f: this.id)
        {
            if(f.getAnnotation(GeneratedValue.class) == null)
            {
                insert.append(f.getAnnotation(Column.class).name() + ", ");
                count++;
            }
        }
        for(Field f: this.attributes)
        {
            insert.append(f.getAnnotation(Column.class).name() + ", ");
            count++;
        }
        insert.delete(insert.length() - 2, insert.length());
        insert.append(")\nVALUES(");
        for(int i = 0; i < count; i++)
            insert.append("?, ");
        insert.delete(insert.length() - 2, insert.length());
        insert.append(")");
        this.insert = new String(insert);

        this.delete = "DELETE FROM " + tableName;

        this.exists = "SELECT EXISTS(SELECT *\n"
                                 +  "FROM " + tableName;
    }

    @Override
    public Optional<T> get(T t) throws SQLException
    {
        boolean isEmpty = isEmpty(t);
        String query = select;
        if(!isEmpty)
            query = addWhere(query, t);

        try(Connection connection = DbConnection.getConnection())
        {
            try(PreparedStatement ps = connection.prepareStatement(query))
            {
                if(!isEmpty)
                    setWhere(ps, t);
                
                //System.out.println(ps);
                ResultSet res = ps.executeQuery();
                if(!res.next())
                  return Optional.empty();
                T newT = BeanUtil.newInstance(beanClass);
                setFields(res, newT);
                return Optional.of(newT);
            }
        }
    }

    @Override
    public List<T> getAll(T t) throws SQLException
    {
        boolean isEmpty = isEmpty(t);
        String query = select;
        if(!isEmpty)
            query = addWhere(query, t);
        
        try(Connection connection = DbConnection.getConnection())
        {
            try(PreparedStatement ps = connection.prepareStatement(query))
            {
                if(!isEmpty)
                    setWhere(ps, t);

                //System.out.println(ps);
                ResultSet res = ps.executeQuery();
                if(!res.next())
                    return List.of();
                List<T> list = new ArrayList<>();
                do
                {
                    T newT = BeanUtil.newInstance(beanClass);
                    setFields(res, newT);
                    list.add(newT);
                }while(res.next());
                return list;
            }
        }
    }

    @Override
    public void save(T t) throws SQLException
    {
        if(t == null)
            throw new NullPointerException();

        if(!isGeneratedIdValid(t) || !areAttributesValid(t))
            throw new IllegalArgumentException();
        
        if(isIdValid(t))
        {
            T template = t;
            if(attributes.size() != 0)
            {
                template = BeanUtil.newInstance(beanClass);
                for(Field f: id)
                    BeanUtil.invokeSetter(template, f, BeanUtil.invokeGetter(t, f));
            }
            if(exists(template))
            {
                update(t, null);
                return;
            }
        }

        try(Connection connection = DbConnection.getConnection())
        {
            try(PreparedStatement ps = connection.prepareStatement(insert))
            {
                int i = 0;
                for(Field f: id)
                {
                    if(f.getAnnotation(GeneratedValue.class) == null)
                        ps.setObject(++i, BeanUtil.invokeGetter(t, f));
                }
                for(int j = 0; j < attributes.size(); j++)
                    ps.setObject(i + j + 1, BeanUtil.invokeGetter(t, attributes.get(j)));
                //System.out.println(ps);
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void update(T t, T newId) throws SQLException
    {
        if(t == null)
            throw new NullPointerException();

        if(!isIdValid(t) || !areAttributesValid(t))
            throw new IllegalArgumentException();
            
        boolean changeId = !isEmpty(newId);
        if(changeId && !isIdValid(newId))
            throw new IllegalArgumentException(); 
        if(!changeId && attributes.size() == 0)
            return;
        
        try(Connection connection = DbConnection.getConnection())
        {
            try(PreparedStatement ps = connection.prepareStatement(update))
            {
                int k = 0;
                for(; k < id.size(); k++)
                    ps.setObject(k + 1, BeanUtil.invokeGetter(changeId ? newId : t, id.get(k)));
                int i = 0;
                for(; i < attributes.size(); i++)
                    ps.setObject(k + i + 1, BeanUtil.invokeGetter(t, attributes.get(i)));
                for(int j = 0; j < id.size(); j++)
                    ps.setObject(k + i + j + 1, BeanUtil.invokeGetter(t, id.get(j)));
                //System.out.println(ps);
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void delete(T t) throws SQLException
    {
        if(isEmpty(t))
            throw new IllegalArgumentException("Delete senza la clausola where è troppo pericolosa, si rischierebbe di cancellare un'intera tabella per sbaglio");

        String query = addWhere(delete, t);
        
        try(Connection connection = DbConnection.getConnection())
        {
            try(PreparedStatement ps = connection.prepareStatement(query))
            {
                setWhere(ps, t);
                //System.out.println(ps);
                ps.executeUpdate();
            }
        }
    }

    @Override
    public boolean exists(T t) throws SQLException
    {
        boolean isEmpty = isEmpty(t);
        String query = exists;
        if(!isEmpty)
            query = addWhere(query, t);
        query += ")";

        try(Connection connection = DbConnection.getConnection())
        {
            try(PreparedStatement ps = connection.prepareStatement(query))
            {
                if(!isEmpty)
                    setWhere(ps, t);

                //System.out.println(ps);
                ResultSet res = ps.executeQuery();
                res.next();
                return res.getBoolean(1);
            }
        }
    }

    protected boolean isIdValid(T t)
    {
        for(Field f: id)
        {
            if(BeanUtil.invokeGetter(t, f) == null)
                return false;
        }
        return true;
    }

    protected boolean areAttributesValid(T t)
    {
        for(Field f: attributes)
        {
            if(!f.getAnnotation(Column.class).nullable() && BeanUtil.invokeGetter(t, f) == null)
                return false;
        }
        return true;
    }

    protected boolean isGeneratedIdValid(T t)
    {
        for(Field f: id)
        {
            if(f.getAnnotation(GeneratedValue.class) == null && BeanUtil.invokeGetter(t, f) == null)
                return false;
        }
        return true;
    }

    private String addWhere(String query, T t)
    {
        StringBuilder sb = new StringBuilder(query);
        sb.append("\nWHERE");
        for(Field f: id)
        {
            if(BeanUtil.invokeGetter(t, f) != null)
                sb.append(" " + f.getAnnotation(Column.class).name() + "=? and");
        }
        for(Field f: attributes)
        {
            if(BeanUtil.invokeGetter(t, f) != null)
                sb.append(" " + f.getAnnotation(Column.class).name() + "=? and");
        }
        sb.delete(sb.length() - 4, sb.length());
        return new String(sb);
    }

    private void setWhere(PreparedStatement ps, T t) throws SQLException
    {
        int i = 0;
        for(Field f: id)
        {
            Object obj = BeanUtil.invokeGetter(t, f);
            if(obj != null)
                ps.setObject(++i, obj);
        }
        for(Field f: attributes)
        {
            Object obj = BeanUtil.invokeGetter(t, f);
            if(obj != null)
                ps.setObject(++i, obj);
        }
    }

    protected boolean isEmpty(T t)
    {
        if(t == null)
            return true;
        for(Field f: id)
        {
            if(BeanUtil.invokeGetter(t, f) != null)
                return false;
        }
        for(Field f: attributes)
        {
            if(BeanUtil.invokeGetter(t, f) != null)
                return false;
        }
        return true;
    }

    protected void setFields(ResultSet res, T t) throws SQLException
    {
        for(Field f: id)
        {
            Object value = res.getObject(f.getAnnotation(Column.class).name(), f.getType());
            BeanUtil.invokeSetter(t, f, value);
        }
        for(Field f: attributes)
        {
            Object value = res.getObject(f.getAnnotation(Column.class).name(), f.getType());
            BeanUtil.invokeSetter(t, f, value);
        }
    }
}