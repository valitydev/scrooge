package dev.vality.scrooge.service.converter;

import dev.vality.scrooge.dao.domain.tables.pojos.Terminal;
import dev.vality.scrooge.domain.TerminalInfo;
import org.springframework.stereotype.Component;

@Component
public class TerminalInfoToTerminalConverter {

    public Terminal convert(TerminalInfo source, Integer providerId) {
        Terminal terminal = new Terminal();
        terminal.setName(source.getName());
        terminal.setDescription(source.getDescription());
        terminal.setProviderId(providerId);
        return terminal;
    }
}
