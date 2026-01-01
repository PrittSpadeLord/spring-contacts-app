package io.github.prittspadelord.application.rest.controllers.v1;

import io.github.prittspadelord.application.rest.annotations.Authorized;
import io.github.prittspadelord.application.rest.models.CreateContactRequest;
import io.github.prittspadelord.application.rest.models.CreateContactResponse;
import io.github.prittspadelord.application.rest.models.DeleteContactResponse;
import io.github.prittspadelord.application.rest.models.GetContactResponse;
import io.github.prittspadelord.application.rest.models.ListContactsResponse;
import io.github.prittspadelord.application.services.ContactService;
import io.github.prittspadelord.application.support.AuthorizationLevel;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
@Slf4j
public class ContactsRestController {

    private final ContactService contactService;

    @Authorized(AuthorizationLevel.USER)
    @PostMapping("/createContact")
    public CreateContactResponse handleContactCreation(@Valid @RequestBody CreateContactRequest createContactRequest, HttpServletRequest request) {
        return this.contactService.createContact(createContactRequest, request);
    }

    @Authorized(AuthorizationLevel.USER)
    @PostMapping("/deleteContact")
    public DeleteContactResponse handleContactDeletion(@RequestParam("id") long contactId, HttpServletRequest request) {
        return this.contactService.deleteContact(contactId, request);
    }

    @Authorized(AuthorizationLevel.USER)
    @GetMapping("/contact")
    public GetContactResponse handleGetContact(@RequestParam("id") long contactId, HttpServletRequest request) {
        return this.contactService.getContact(contactId, request);
    }

    @Authorized(AuthorizationLevel.USER)
    @GetMapping("/contacts")
    public ListContactsResponse handleListContact(HttpServletRequest request) {
        return this.contactService.listContacts(request);
    }
}