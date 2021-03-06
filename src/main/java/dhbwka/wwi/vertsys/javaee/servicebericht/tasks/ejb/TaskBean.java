/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.servicebericht.tasks.ejb;

import dhbwka.wwi.vertsys.javaee.servicebericht.common.ejb.EntityBean;
import dhbwka.wwi.vertsys.javaee.servicebericht.tasks.jpa.Category;
import dhbwka.wwi.vertsys.javaee.servicebericht.tasks.jpa.Task;
import dhbwka.wwi.vertsys.javaee.servicebericht.tasks.jpa.TaskStatus;
import dhbwka.wwi.vertsys.javaee.servicebericht.tasks.jpa.TaskToRet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Einfache EJB mit den üblichen CRUD-Methoden für Aufgaben
 */
@Stateless
@RolesAllowed("app-user")
public class TaskBean extends EntityBean<Task, Long> {

    public TaskBean() {
        super(Task.class);
    }

    /**
     * Alle Aufgaben eines Benutzers, nach Fälligkeit sortiert zurückliefern.
     *
     * @param username Benutzername
     * @return Alle Aufgaben des Benutzers
     */
    public List<TaskToRet> findByStatus(TaskStatus Status) {
        List<Task> tasks = em.createQuery("SELECT t FROM Task t WHERE t.status = :Status")
                .setParameter("Status", Status)
                .getResultList();

        List<TaskToRet> tasksToRet = new ArrayList<>();
        for (Task task : tasks) {
            TaskToRet taskRet = new TaskToRet(task.getId(), task.getCategory(), task.getShortText(), task.getLongText());
            tasksToRet.add(taskRet);
        }
        return tasksToRet;
    

}
public List<Task> findByUsername(String username) {
        return em.createQuery("SELECT t FROM Task t WHERE t.owner.username = :username")
                 .setParameter("username", username)
                 .getResultList();
    }
      public List<TaskToRet> getTasks() {
          
            String select = "SELECT t FROM Task t";
       
            List<Task> tasks = em.createQuery(select).getResultList();
            List<TaskToRet> tasksToRet = new ArrayList<>();
        for (Task task : tasks) {
            TaskToRet taskRet= new TaskToRet(task.getId(),task.getCategory(), task.getShortText(), task.getLongText());
            tasksToRet.add(taskRet);
        }
            return tasksToRet;
      }
    /**
     * Suche nach Aufgaben anhand ihrer Bezeichnung, Kategorie und Status.
     * 
     * Anders als in der Vorlesung behandelt, wird die SELECT-Anfrage hier
     * mit der CriteriaBuilder-API vollkommen dynamisch erzeugt.
     * 
     * @param search In der Kurzbeschreibung enthaltener Text (optional)
     * @param category Kategorie (optional)
     * @param status Status (optional)
     * @return Liste mit den gefundenen Aufgaben
     */
    public List<Task> search(String search, Category category, TaskStatus status) {
        // Hilfsobjekt zum Bauen des Query
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        
        // SELECT t FROM Task t
        CriteriaQuery

<Task> query = cb.createQuery(Task.class  

    );
        Root<Task> from = query.from(Task.class);

    query.select (from);

    // WHERE t.shortText LIKE :search
    Predicate p = cb.conjunction();

    if (search

    != null && !search.trim () 
        .isEmpty()) {
            p = cb.and(p, cb.like(from.get("shortText"), "%" + search + "%"));
        query.where(p);
    }

    // WHERE t.category = :category
    if (category

    
        != null) {
            p = cb.and(p, cb.equal(from.get("category"), category));
        query.where(p);
    }

    // WHERE t.status = :status
    if (status

    
        != null) {
            p = cb.and(p, cb.equal(from.get("status"), status));
        query.where(p);
    }

    return em.createQuery (query)

.getResultList();
    }
}
