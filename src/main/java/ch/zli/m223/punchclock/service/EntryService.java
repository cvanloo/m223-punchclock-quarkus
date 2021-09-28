package ch.zli.m223.punchclock.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import ch.zli.m223.punchclock.domain.Entry;

@ApplicationScoped
public class EntryService {
    @Inject
    private EntityManager entityManager;

    public EntryService() {
    }

    @Transactional 
    public Entry createEntry(Entry entry) {
        entityManager.persist(entry);
        return entry;
    }

    public Entry getEntryById(Long id) {
        var query = entityManager.createQuery("FROM Entry WHERE id = :id").setParameter("id", id);
        return (Entry) query.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<Entry> getAllEntries() {
        var query = entityManager.createQuery("FROM Entry");
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Entry> getAllEntriesFromUser(Long userId) {
        var query = entityManager.createQuery("FROM Entry WHERE user_id = :userId").setParameter("userId", userId);
        return query.getResultList();
    }

    @Transactional
    public boolean deleteEntry(Long id) {
        Entry entry = entityManager.find(Entry.class, id);

        if (null == entry) {
            return false;
        }

        entityManager.remove(entry);

        return true;
    }

    @Transactional
    public Entry updateEntry(Entry entry) {
        return entityManager.merge(entry);
    }
}
