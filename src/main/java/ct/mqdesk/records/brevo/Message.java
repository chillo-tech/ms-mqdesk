package ct.mqdesk.records.brevo;

import java.util.Set;

public record Message(
        String subject,
        String htmlContent,
        Contact sender,
        Set<Contact> to
) {
}
