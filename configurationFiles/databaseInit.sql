drop schema spark;

create schema spark;

use spark;

create table amministratore (
	codice smallint auto_increment not null,
	email varchar(45) not null,
	password char(32) not null,
	nome varchar(30) not null,
	cognome varchar(30) not null,
	primary key(codice),
	unique(email)
);

create table cliente (
	codice int auto_increment not null,
	email varchar(45) not null,
	password char(32) not null,
	nome varchar(30) not null,
	cognome varchar(30) not null,
	numAcquisti int not null,
	primary key(codice),
	unique(email)
);

create table carta (
	numero varchar(16) not null,
	nome varchar(30) not null,
	cognome varchar(30) not null,
	scadenza date not null,
	cvc char(4) not null,
	cliente int not null,
	primary key(numero),
	constraint possesso_carta foreign key possesso_carta_idx (cliente) references cliente(codice) on update cascade on delete cascade
);

create table indirizzo (
	numProgressivo int auto_increment not null,
	numCivico smallint not null,
	via varchar(45) not null,
	cap char(5) not null,
	nome varchar(30) not null,
	cognome varchar(30) not null,
	cliente int,
	primary key(numProgressivo),
	constraint possesso foreign key possesso_idx (cliente) references cliente(codice) on update cascade on delete cascade
);

create table ordine (
	numFattura int auto_increment not null,
	numCarta varchar(16) not null,
	data date not null,
	prezzoTot decimal(8,2) not null,
	indirizzo int not null,
	cliente int not null,
	primary key(numFattura),
	constraint realizzazione foreign key realizzazione_idx (cliente) references cliente(codice) on update cascade on delete cascade,
	constraint spedizione foreign key spedizione_idx (indirizzo) references indirizzo(numProgressivo) on update cascade on delete cascade
);

create table produttore (
	nome varchar(30) not null,
	primary key(nome)
);

create table prodotto (
	numProgressivo int auto_increment not null,
	nome varchar(30) not null,
	icona varchar(255),
	cancellato boolean not null,
	produttore varchar(30) not null,
	primary key(numProgressivo),
	unique(nome),
	constraint produzione foreign key produzione_idx (produttore) references produttore(nome) on update cascade on delete restrict
);

create table categoria (
	numProgressivo smallint auto_increment not null,
	tipo varchar(30) not null,
	sottotipo varchar(30) not null,
	primary key(numProgressivo),
	unique(tipo, sottotipo)
);

create table appartenenza (
	prodotto int not null,
	categoria smallint not null,
	primary key(prodotto, categoria),
	constraint appartenenza_prodotto foreign key appartenenza_prodotto_idx (prodotto) references prodotto(numProgressivo) on update cascade on delete cascade,
	constraint appartenenza_categoria foreign key appartenenza_categoria_idx (categoria) references categoria(numProgressivo) on update cascade on delete cascade
);

create table versione (
	codice varchar(50) not null,
	nome varchar(30) not null,
	icona varchar(255),
	cancellata boolean not null,
	prodotto int not null,
	primary key(codice),
	constraint associazione foreign key associazione_idx (prodotto) references prodotto(numProgressivo) on update cascade on delete restrict
);

create table datiVersione (
	versione varchar(50) not null,
	dataDiUscita date not null,
	descrizione varchar(2048) not null,
	iva decimal(5,2) not null,
	prezzo decimal(8,2) not null,
	quantita smallint not null,
	primary key(versione),
	constraint caratterizzazione foreign key caratterizzazione_idx (versione) references versione(codice) on update cascade on delete cascade
);

create table immagine (
	versione varchar(50) not null,
	numProgressivo smallint not null,
	img varchar(255) not null,
	primary key(versione, numProgressivo),
	constraint comprensione foreign key comprensione_idx (versione) references datiVersione(versione) on update cascade on delete cascade
);

create table composizione (
	versione varchar(50) not null,
	ordine int not null,
	prezzo decimal(8,2) not null,
	quantita smallint not null,
	iva decimal(5,2) not null,
	primary key(versione, ordine),
	constraint composizione_versione foreign key composizione_versione_idx (versione) references versione(codice) on update cascade on delete restrict,
	constraint composizione_ordine foreign key composizione_ordine_idx (ordine) references ordine(numFattura) on update cascade on delete cascade
);

create table wishlist (
	versione varchar(50) not null,
	cliente int not null,
	primary key(versione, cliente),
	constraint wishlist_versione foreign key wishlist_versione_idx (versione) references versione(codice) on update cascade on delete cascade,
	constraint wishlist_cliente foreign key wishlist_cliente_idx (cliente) references cliente(codice) on update cascade on delete cascade
);

create table carrello (
	versione varchar(50) not null,
	cliente int not null,
	quantita smallint not null,
	primary key(versione, cliente),
	constraint carrello_versione foreign key carrello_versione_idx (versione) references versione(codice) on update cascade on delete cascade,
	constraint carrello_cliente foreign key carrello_cliente_idx (cliente) references cliente(codice) on update cascade on delete cascade
);

create table recensione (
	versione varchar(50) not null,
	cliente int not null,
	voto tinyint not null,
	testo varchar(512) not null,
	primary key(versione, cliente),
	constraint recensione_versione foreign key recensione_versione_idx (versione) references versione(codice) on update cascade on delete cascade,
	constraint recensione_cliente foreign key recensione_cliente_idx (cliente) references cliente(codice) on update cascade on delete cascade
);