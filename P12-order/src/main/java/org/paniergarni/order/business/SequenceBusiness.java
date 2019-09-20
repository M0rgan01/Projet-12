package org.paniergarni.order.business;

import org.paniergarni.order.entities.Sequence;
import org.paniergarni.order.exception.SequenceException;

import java.util.Date;

public interface SequenceBusiness {

    Sequence createSequence();
    Sequence getById(Long id) throws SequenceException;
    Sequence getByDate(Date date) throws SequenceException;
    void addSaleNumber(Date date) throws SequenceException;
}
