package ru.otus.messagesystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.messagesystem.message.model.MessageClientId;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MessageClientStorage {
    private static final Logger logger = LoggerFactory.getLogger(MessageClientStorage.class);
    private final Map<String, BalancedPortList> clientStorage = new ConcurrentHashMap<>();

    public synchronized void addClient(MessageClientId messageClientId) {
        if (clientStorage.containsKey(messageClientId.getName())) {
            clientStorage.get(messageClientId.getName()).registerClient(messageClientId);
            return;
        }
        BalancedPortList balancedPortList = new BalancedPortList();
        balancedPortList.registerClient(messageClientId);
        clientStorage.put(messageClientId.getName(), balancedPortList);
    }

    public Optional<MessageClientId> getClientIdByName(String clientName) {
        if (!clientStorage.containsKey(clientName)) {
            return Optional.empty();
        }
        return Optional.of(clientStorage.get(clientName).getRegisteredClient());
    }

    public synchronized void removeClient(MessageClientId messageClientId) {
        if (clientStorage.containsKey(messageClientId.getName())) {
            clientStorage.get(messageClientId.getName()).removeRegisteredClient(messageClientId);
        }
    }

    private class BalancedPortList {
        private int nextClientNumber = 0;
        private final List<MessageClientId> registeredPorts = new CopyOnWriteArrayList<>();

        public void registerClient(MessageClientId messageClientId) {
            registeredPorts.add(messageClientId);
        }

        public synchronized MessageClientId getRegisteredClient() {
            if (registeredPorts.size() == 0)
                return null;
            if (nextClientNumber == registeredPorts.size())
                nextClientNumber = 0;
            return registeredPorts.get(nextClientNumber++);
        }

        public synchronized void removeRegisteredClient(MessageClientId messageClientId) {
            registeredPorts.remove(messageClientId);
        }
    }
}
