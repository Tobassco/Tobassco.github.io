#include <stdio.h>
#include <stdlib.h>
#include "client.h"

// Data structure to hold client state.
typedef struct Client {

} Client;

ClientImplementation* client_init() {
    void* ptr = malloc(sizeof(Client));
    return ptr;
}

bool client_connect(ClientImplementation* client, const char* addr, uint16_t port, uint32_t game_id) {
    return true;
}

bool client_wait_for_opponent(ClientImplementation* client) {
    return true;
}

int8_t client_send_ships(ClientImplementation* client, const struct Ship (*ships)[4]) {
    return -1;
}

TurnResult client_send_move(ClientImplementation* client, const char* coordinate) {
    return Invalid;
}

ExtendedTurnResult client_send_move_extended(ClientImplementation* client, const char* coordinate) {
    ExtendedTurnResult result = {
        .length = 0,
        .data = NULL,
    };
    return result;
}

MoveResult client_receive_move(ClientImplementation* client) {
    MoveResult result = {
        .coordinate = NULL,
        .result = Invalid,
    };
    return result;
}

void client_free_extended_result(ExtendedTurnResult result) {
}

void client_free_move_result(MoveResult result) {
}

void client_close(ClientImplementation* client) {
    free(client);
}
