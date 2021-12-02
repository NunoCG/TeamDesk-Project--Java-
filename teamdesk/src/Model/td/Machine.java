package Model.td;

import Model.td.database.DatabaseConnect;
import Model.td.database.DatabaseMachine;

import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Public class Machine with it's attributes
 */
public class Machine {

    private String id;
    private OperationType operationType;
    private String description;
    private boolean status;
    private int timeWork;
    private String infoOpType;
    private ArrayList<Schedule> listSchedule;
    private int idSemana;

    /**
     * Empty constructor
     */
    public Machine() {
    }

    /**
     * Main class Machine constructor with all it's attributes as parameters
     *
     * @param id
     * @param operationType
     * @param description
     * @param status
     * @param timeWork
     */
    public Machine(String id, OperationType operationType, String description, boolean status, int timeWork, String infoOpType, ArrayList<Schedule> listSchedule, int idSemana) {

        this.id = id;
        this.operationType = operationType;
        this.description = description;
        this.status = status;
        this.timeWork = timeWork;
        this.infoOpType = infoOpType;
        this.listSchedule = listSchedule;
        this.idSemana = idSemana;
    }

    /**
     * get method of ID
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * set method of ID
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * get method of operation type
     *
     * @return operation type
     */
    public OperationType getOperationType() {
        return operationType;
    }

    /**
     * set method of operation type
     *
     * @param operationType
     */
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    /**
     * get method of description
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set method of description
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Iis (boolean) method of status
     *
     * @return status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * set method of status
     *
     * @param status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * get method of timework
     *
     * @return tempo de trabalho
     */
    public int getTimeWork() {
        return timeWork;
    }

    /**
     * set method of timework
     *
     * @param timeWork
     */
    public void setTimeWork(int timeWork) {
        this.timeWork = timeWork;
    }

    /**
     *
     * @return
     */
    public String getInfoOpType() {
        return infoOpType;
    }

    /**
     *
     * @param infoOpType
     */
    public void setInfoOpType(String infoOpType) {
        this.infoOpType = infoOpType;
    }

    /**
     *
     * @return
     */
    public ArrayList<Schedule> getListSchedule() {
        return listSchedule;
    }

    /**
     *
     * @param listSchedule
     */
    public void setListSchedule(ArrayList<Schedule> listSchedule) {
        this.listSchedule = listSchedule;
    }

    /**
     *
     * @return
     */
    public int getIdSemana() {
        return idSemana;
    }

    /**
     *
     * @param idSemana
     */
    public void setIdSemana(int idSemana) {
        this.idSemana = idSemana;
    }

    /**
     * toString method
     *
     * @return toString format
     */
    @Override
    public String toString() {
        return String.format("--------------------------------\n" +
                        "ID Máquina: %s\nTipo de Operação: %s\nDescrição: %s\nEstado: %s\nTempo de trabalho: %d\n",
                this.getId(), this.getOperationType(), this.getDescription(), this.isStatus(), this.getTimeWork());
    }
}
