module org.pmd.pizzeria_ms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires org.hibernate.commons.annotations;
    requires org.jboss.jandex;
    requires java.persistence;

    opens org.pmd.pizzeria_ms.controller to javafx.fxml, javafx.base;
    opens org.pmd.pizzeria_ms.model to org.hibernate.orm.core;
    exports org.pmd.pizzeria_ms.controller;
    exports org.pmd.pizzeria_ms.view;
    exports org.pmd.pizzeria_ms.dao;
    exports org.pmd.pizzeria_ms.model;
    exports org.pmd.pizzeria_ms.jdbc;
    exports org.pmd.pizzeria_ms.enumeration;
}
