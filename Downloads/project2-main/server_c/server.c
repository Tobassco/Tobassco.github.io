#include <stdio.h>
#include <assert.h>
#include "engine.h"

// Example usage of engine.
// Use this as a reference only.
int main(int argc, char* argv[]) {
    Engine* engine = engine_init();
    assert(engine != NULL);

    assert(engine_init_game(engine, 0));

    Ship ships_player1[4] = {
        {"A1", 5, Horizontal},
        {"A2", 4, Vertical},
        {"C2", 3, Horizontal},
        {"G1", 2, Vertical},
    };
    int result = engine_place_ships(engine, 0, &ships_player1);
    assert(result == 1);

    Ship ships_player2[4] = {
        {"B1", 5, Horizontal},
        {"B2", 4, Vertical},
        {"D2", 3, Horizontal},
        {"H1", 2, Vertical},
    };
    int result2 = engine_place_ships(engine, 0, &ships_player2);
    assert(result2 == 2);

    // Ships were already placed
    int result3 = engine_place_ships(engine, 0, &ships_player1);
    assert(result3 == -1);

    assert(engine_take_turn(engine, 0, 1, "B1") == Hit);
    assert(engine_take_turn(engine, 0, 2, "J10") == Miss);
    assert(engine_take_turn(engine, 0, 1, "C1") == Hit);
    assert(engine_take_turn(engine, 0, 2, "J9") == Miss);
    assert(engine_take_turn(engine, 0, 1, "D1") == Hit);
    assert(engine_take_turn(engine, 0, 2, "J8") == Miss);
    assert(engine_take_turn(engine, 0, 1, "E1") == Hit);
    assert(engine_take_turn(engine, 0, 2, "J7") == Miss);
    assert(engine_take_turn(engine, 0, 1, "F1") == Sunk5);
    assert(engine_take_turn(engine, 0, 2, "J6") == Miss);

    // Repeat of previous coordinate
    assert(engine_take_turn(engine, 0, 1, "F1") == Invalid);
    // Invalid coordinate
    assert(engine_take_turn(engine, 0, 1, "COMP30023") == Invalid);
    assert(engine_take_turn(engine, 0, 1, NULL) == Invalid);
    assert(engine_take_turn(engine, 0, 1, "") == Invalid);
    // Not player 2's turn
    assert(engine_take_turn(engine, 0, 2, "J5") == Invalid);

    assert(engine_take_turn(engine, 0, 1, "B2") == Hit);
    assert(engine_take_turn(engine, 0, 2, "J5") == Miss);
    assert(engine_take_turn(engine, 0, 1, "B3") == Hit);
    assert(engine_take_turn(engine, 0, 2, "J4") == Miss);
    assert(engine_take_turn(engine, 0, 1, "B4") == Hit);
    assert(engine_take_turn(engine, 0, 2, "J3") == Miss);
    assert(engine_take_turn(engine, 0, 1, "B5") == Sunk4);
    assert(engine_take_turn(engine, 0, 2, "J2") == Miss);

    assert(engine_take_turn(engine, 0, 1, "D2") == Hit);
    assert(engine_take_turn(engine, 0, 2, "J1") == Miss);
    assert(engine_take_turn(engine, 0, 1, "E2") == Hit);
    assert(engine_take_turn(engine, 0, 2, "I10") == Miss);
    assert(engine_take_turn(engine, 0, 1, "F2") == Sunk3);
    assert(engine_take_turn(engine, 0, 2, "I9") == Miss);

    assert(engine_take_turn(engine, 0, 1, "H1") == Hit);
    assert(engine_take_turn(engine, 0, 2, "I8") == Miss);
    TurnResult final_result = engine_take_turn(engine, 0, 1, "H02");
    assert(final_result == Win);
    assert(final_result != Sunk2);

    ExtendedTurnResult invalid_extended_result = engine_take_turn_extended(engine, 0, 1, "Z9");
    assert(invalid_extended_result.length == 0);
    assert(invalid_extended_result.data == NULL);

    TurnResult extracted_result = engine_extract_turn_result(invalid_extended_result);
    assert(extracted_result == Invalid);

    engine_free_extended_result(invalid_extended_result);

    engine_end_game(engine, 0);

    engine_free(engine);
    return 0;
}
