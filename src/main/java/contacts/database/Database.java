package contacts.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.*;
import java.util.List;

public class Database {
    private final EntityManagerFactory ENTITY_MANAGER_FACTORY;

    public Database() {
        this.ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("contacts-persistence-unit");
    }

    public void closeEntityManagerFactory() {
        ENTITY_MANAGER_FACTORY.close();
    }

    public void saveContact(Contact contact) throws DatabaseOperationException {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(contact);
            transaction.commit();
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DatabaseOperationException("Could not save " + contact.getName() + " to the database.");
        } finally {
            entityManager.close();
        }
    }

    public void deleteContact(Contact contact) throws DatabaseOperationException {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            Contact persistentContact = entityManager.find(Contact.class, contact.getId());
            entityManager.remove(persistentContact);
            transaction.commit();
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DatabaseOperationException("Could not delete given contact");
        } finally {
            entityManager.close();
        }
    }

    public void updateContact(Contact updatedContact) throws DatabaseOperationException {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.merge(updatedContact);
            transaction.commit();
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DatabaseOperationException("Could not update given contact");
        } finally {
            entityManager.close();
        }
    }

    public List<Contact> getAllContacts() throws DatabaseOperationException {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Contact> criteria = builder.createQuery(Contact.class);
            Root<Contact> root = criteria.from(Contact.class);
            criteria.select(root);
            criteria.orderBy(builder.asc(root.get("name")));
            return entityManager.createQuery(criteria).getResultList();
        } catch (Exception exception) {
            throw new DatabaseOperationException("Could not load contacts from the database");
        } finally {
            entityManager.close();
        }
    }

    public Contact getByName(String name) throws DatabaseOperationException {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Contact> criteria = builder.createQuery(Contact.class);
            Root<Contact> root = criteria.from(Contact.class);
            Predicate predicate = builder.equal(root.get("name"), name);
            criteria.select(root).where(predicate);
            return entityManager.createQuery(criteria).getSingleResult();
        } catch (Exception exception) {
            throw new DatabaseOperationException("Could not get given contact");
        } finally {
            entityManager.close();
        }
    }

    public List<Contact> searchByPattern(String pattern) throws DatabaseOperationException {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Contact> criteria = builder.createQuery(Contact.class);
            Root<Contact> root = criteria.from(Contact.class);

            String criteriaPattern = ("%" + pattern + "%").toUpperCase();
            Predicate namePredicate = builder.like(builder.upper(root.get("name")), criteriaPattern);
            Predicate numberPredicate = builder.like(root.get("number"), criteriaPattern);
            Predicate emailPredicate = builder.like(builder.upper(root.get("email")), criteriaPattern);

            criteria.select(root).where(builder.or(namePredicate, numberPredicate, emailPredicate));
            return entityManager.createQuery(criteria).getResultList();
        } catch (Exception exception) {
            throw new DatabaseOperationException("Could not search for contacts in the database");
        } finally {
            entityManager.close();
        }
    }
}
