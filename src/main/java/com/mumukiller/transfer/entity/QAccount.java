package com.mumukiller.transfer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QAccount is a Querydsl query type for QAccount
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QAccount extends com.querydsl.sql.RelationalPathBase<Account> {

    private static final long serialVersionUID = -1759088447;

    public static final QAccount account = new QAccount("ACCOUNT");

    public final StringPath currency = createString("currency");

    public final StringPath id = createString("id");

    public final StringPath username = createString("username");

    public final NumberPath<java.math.BigDecimal> value = createNumber("value", java.math.BigDecimal.class);

    public QAccount(String variable) {
        super(Account.class, forVariable(variable), "PUBLIC", "ACCOUNT");
        addMetadata();
    }

    public QAccount(String variable, String schema, String table) {
        super(Account.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QAccount(String variable, String schema) {
        super(Account.class, forVariable(variable), schema, "ACCOUNT");
        addMetadata();
    }

    public QAccount(Path<? extends Account> path) {
        super(path.getType(), path.getMetadata(), "PUBLIC", "ACCOUNT");
        addMetadata();
    }

    public QAccount(PathMetadata metadata) {
        super(Account.class, metadata, "PUBLIC", "ACCOUNT");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(currency, ColumnMetadata.named("CURRENCY").withIndex(4).ofType(Types.VARCHAR).withSize(10));
        addMetadata(id, ColumnMetadata.named("ID").withIndex(1).ofType(Types.VARCHAR).withSize(100));
        addMetadata(username, ColumnMetadata.named("USERNAME").withIndex(2).ofType(Types.VARCHAR).withSize(10));
        addMetadata(value, ColumnMetadata.named("VALUE").withIndex(3).ofType(Types.DECIMAL).withSize(65535).withDigits(32767));
    }

}

