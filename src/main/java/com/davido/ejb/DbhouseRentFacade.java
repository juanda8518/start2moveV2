/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davido.ejb;

import com.davido.entities.DbhouseRent;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author davidortega
 */
@Stateless
public class DbhouseRentFacade extends AbstractFacade<DbhouseRent> implements DbhouseRentFacadeLocal {

    @PersistenceContext(unitName = "com.davido_LookAndFeel_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DbhouseRentFacade() {
        super(DbhouseRent.class);
    }

    @Override
    public List<Object[]> findHouseRentPostCode() {
        String querySTR;
        List<Object[]> resultantList = new ArrayList<>();
        try {
            querySTR = "SELECT DISTINCT ROUND(AVG(a.houseRentPrice)) "
                    + "FROM db_houseRent a, db_postCode b "
                    + "WHERE a.postCode = b.postCodeId "
                    + "AND a.postLine = b.postCodeLine "
                    + "GROUP BY a.postCode, b.postCodeName "
                    + "ORDER BY 1";
            Query query = em.createNativeQuery(querySTR);
            resultantList = query.getResultList();
        } catch (Exception e) {
            System.err.println(e);
        }
        return resultantList;
    }

    @Override
    public List<Object[]> findHouseRent(List<String> listOfPostCodes) {
        String querySTR;
        List<Object[]> resultantList = new ArrayList<>();
        try {
            querySTR = "SELECT MIN(rentPrice), MAX(rentPrice) "
                    + "FROM (SELECT ROUND(AVG(a.houseRentPrice)) rentPrice "
                    + "FROM db_houseRent a "
                    + "WHERE a.postCode IN ?1 "
                    + "GROUP BY a.postCode) a";
            Query query = em.createNativeQuery(querySTR);
            query.setParameter(1, listOfPostCodes);
            resultantList = query.getResultList();
        } catch (Exception e) {
            System.err.println(e);
        }
        return resultantList;
    }

    @Override
    public List<Object[]> getAllHouseRent(List<String> listOfPostCodes) {
        String querySTR;
        List<Object[]> resultantList = new ArrayList<>();
        try {
            querySTR = "SELECT DISTINCT a.postCode, b.postCodeName, ROUND(AVG(a.houseRentPrice)) "
                    + "FROM db_houseRent a, db_postCode b "
                    + "WHERE a.postCode = b.postCodeId "
                    + "AND a.postLine = b.postCodeLine "
                    + "AND a.postCode IN ?1 "
                    + "GROUP BY a.postCode, b.postCodeName "
                    + "ORDER BY 3";
            Query query = em.createNativeQuery(querySTR);
            query.setParameter(1, listOfPostCodes);
            resultantList = query.getResultList();
        } catch (Exception e) {
            System.err.println(e);
        }
        return resultantList;
    }

    @Override
    public List<DbhouseRent> findByPostcode(int postcode) {
        String querySTR;
        List<DbhouseRent> resultantList = new ArrayList<>();
        try {
            querySTR = "FROM DbhouseRent hr WHERE hr.dbpostCode.dbpostCodePK.postCodeId = ?1";
            Query query = em.createQuery(querySTR);
            query.setParameter(1, postcode);
            resultantList = query.getResultList();
        } catch (Exception e) {
            System.err.println(e);
        }
        return resultantList;
    }

    @Override
    public List<Object[]> findPostCodeByRange(String min, String max) {
        String querySTR;
        List<Object[]> resultantList = new ArrayList<>();
        try {
            querySTR = "SELECT DISTINCT b.postCodeId, b.postCodeLine, b.postCodeName, ROUND(AVG(a.houseRentPrice)) rentPrice "
                    + "FROM db_houseRent a, db_postCode b "
                    + "WHERE a.postCode = b.postCodeId "
                    + "  AND a.postLine = b.postCodeLine "
                    + "  GROUP BY b.postCodeId, b.postCodeLine, b.postCodeName "
                    + "  HAVING rentPrice >= ?1 AND rentPrice <= ?2 "
                    + "  ORDER BY 4";
            Query query = em.createNativeQuery(querySTR);
            query.setParameter(1, min);
            query.setParameter(2, max);
            resultantList = query.getResultList();
        } catch (Exception e) {
            System.err.println(e);
        }
        return resultantList;
    }

    @Override
    public List<Object[]> findByPostcodeAndLine(String postcode, String postCodeLine) {
        String querySTR;
        List<Object[]> resultantList = new ArrayList<>();
        try {
            querySTR = "SELECT a.postCode, a.postLine, a.year, ROUND(AVG(a.houseRentPrice)) rentPrice "
                    + "FROM db_houseRent a "
                    + "WHERE a.postCode = ?1 "
                    + "AND a.postLine = ?2 "
                    + "GROUP BY a.postCode, a.postLine, a.year "
                    + "ORDER BY 3 ";
            Query query = em.createNativeQuery(querySTR);
            query.setParameter(1, postcode);
            query.setParameter(2, postCodeLine);
            resultantList = query.getResultList();
        } catch (Exception e) {
            System.err.println(e);
        }
        return resultantList;
    }

}
