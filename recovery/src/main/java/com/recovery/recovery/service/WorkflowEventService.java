package com.recovery.recovery.service;

import org.springframework.stereotype.Service;

import com.recovery.recovery.entity.Checkout;
import com.recovery.recovery.entity.WorkflowEvent;
import com.recovery.recovery.enums.EventType;
import com.recovery.recovery.repository.WorkflowEventRepository;

@Service
public class WorkflowEventService {

    private final WorkflowEventRepository workflowEventRepository;

    public WorkflowEventService(WorkflowEventRepository workflowEventRepository) {
        this.workflowEventRepository = workflowEventRepository;
    }

    public WorkflowEvent logEvent(Checkout checkout, EventType eventType, String eventData) {
        WorkflowEvent event = new WorkflowEvent();
        event.setCheckout(checkout);
        event.setEventType(eventType);
        event.setEventData(eventData);
        return workflowEventRepository.save(event);
    }
}
