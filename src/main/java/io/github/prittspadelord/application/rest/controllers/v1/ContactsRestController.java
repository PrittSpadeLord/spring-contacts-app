package io.github.prittspadelord.application.rest.controllers.v1;

import io.github.prittspadelord.application.rest.annotations.Authorized;
import io.github.prittspadelord.application.rest.models.CreateContactRequest;
import io.github.prittspadelord.application.rest.models.CreateContactResponse;
import io.github.prittspadelord.application.rest.models.DeleteContactResponse;
import io.github.prittspadelord.application.rest.models.GetContactResponse;
import io.github.prittspadelord.application.rest.models.ListContactsResponse;
import io.github.prittspadelord.application.rest.models.UpdateContactRequest;
import io.github.prittspadelord.application.rest.models.UpdateContactResponse;
import io.github.prittspadelord.application.services.ContactService;
import io.github.prittspadelord.application.support.AuthorizationLevel;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        long userId = (Long) request.getAttribute("user_id");
        return this.contactService.createContact(createContactRequest, userId);
    }

    @Authorized(AuthorizationLevel.USER)
    @PostMapping("/deleteContact")
    public DeleteContactResponse handleContactDeletion(@RequestParam("id") long contactId, HttpServletRequest request) {
        long userId = (Long) request.getAttribute("user_id");
        return this.contactService.deleteContact(contactId, userId);
    }

    @Authorized(AuthorizationLevel.USER)
    @GetMapping("/contact")
    public GetContactResponse handleGetContact(@RequestParam("id") long contactId, HttpServletRequest request) {
        long userId = (Long) request.getAttribute("user_id");
        return this.contactService.getContact(contactId, userId);
    }

    @Authorized(AuthorizationLevel.USER)
    @GetMapping("/contacts")
    public ListContactsResponse handleListContact(HttpServletRequest request) {
        long userId = (Long) request.getAttribute("user_id");
        return this.contactService.listContacts(userId);
    }

    @Authorized(AuthorizationLevel.USER)
    @PutMapping("/updateContact")
    public UpdateContactResponse handleUpdateContact(@RequestParam("id") long contactId, @Valid @RequestBody UpdateContactRequest updateContactRequest, HttpServletRequest request) {
        long userId = (Long) request.getAttribute("user_id");
        return this.contactService.updateContactResponse(updateContactRequest, contactId, userId);
    }
}