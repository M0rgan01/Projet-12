package org.paniergarni.order.business;

import org.paniergarni.order.dao.SequenceRepository;
import org.paniergarni.order.entities.Sequence;
import org.paniergarni.order.exception.SequenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class SequenceBusinessImpl implements SequenceBusiness {

    @Autowired
    private SequenceRepository sequenceRepository;

    @Override
    public Sequence createSequence() {
        Calendar calendar = Calendar.getInstance();
        Sequence sequence = new Sequence();
        sequence.setDate(calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR));
        sequence.setSequence(1);
        return sequenceRepository.save(sequence);
    }

    @Override
    public Sequence getById(Long id) throws SequenceException {
        return sequenceRepository.findById(id).orElseThrow(() -> new SequenceException("sequence.id.incorrect"));
    }

    @Override
    public Sequence getByDate(Date date) throws SequenceException{
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return sequenceRepository.getByDateEquals(calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR)).orElseThrow(() -> new SequenceException("sequence.date.incorrect"));
    }

    @Override
    public void addSaleNumber(Date date) throws SequenceException {
        Sequence sequence = getByDate(date);
        sequence.setSaleNumber(sequence.getSaleNumber() + 1);
        sequenceRepository.save(sequence);
    }
}
