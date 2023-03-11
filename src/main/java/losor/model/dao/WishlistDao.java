package losor.model.dao;

import losor.model.bean.Wishlist;

public class WishlistDao extends AbstractDao<Wishlist>
{
    private static final WishlistDao instance = new WishlistDao();

    private WishlistDao()
    {
        super(Wishlist.class);
    }

    public static WishlistDao getInstance()
    {
        return instance;
    }
}