package com.vmg.scrum.service;

import com.vmg.scrum.payload.request.OfferRequest;
import com.vmg.scrum.payload.response.MessageResponse;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface OfferRequestService {
    MessageResponse addRequest(OfferRequest offerRequest)throws MessagingException, UnsupportedEncodingException;
}
