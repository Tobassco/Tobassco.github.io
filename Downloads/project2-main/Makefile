LIBENGINE=project2-bin/libengine.a
LIBRUNNER=project2-bin/librunner.a

CC=cc
CFLAGS=
LDFLAGS=

all: client.a server.a client_rs.a server_rs.a

# C
# Create executables by linking the static libraries
client: $(LIBRUNNER) client.a
	$(CC) $(CFLAGS) -o $@ $^ $(LDFLAGS)

server: server.a $(LIBENGINE)
	$(CC) $(CFLAGS) -o $@ $^ $(LDFLAGS)

# Create static libraries from .o files
client.a: client_c/client.o
	rm -f $@
	ar rcs $@ $^

server.a: server_c/server.o
	rm -f $@
	ar rcs $@ $^

# Rule to compile any .o file
%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

# Rust
RUSTC=rustc
RUSTFLAGS=-O -C codegen-units=1 -C panic=abort

# Create executables by linking the static libraries
client_rust: $(LIBRUNNER) client_rs.a
	$(CC) -Wl,--allow-multiple-definition $(CFLAGS) -o $@ $^ $(LDFLAGS)

server_rust: server_rs.a $(LIBENGINE)
	$(CC) $(CFLAGS) -o $@ $^ $(LDFLAGS)

# Create static libraries from .rs files
client_rs.a: client_rs/src/*.rs
	$(RUSTC) $(RUSTFLAGS) --crate-type=staticlib -o client_rs.a client_rs/src/lib.rs

server_rs.a: server_rs/src/*.rs
	$(RUSTC) $(RUSTFLAGS) --emit=obj -o server_rs/server.o server_rs/src/main.rs
	rm -f $@
	ar rcs server_rs.a server_rs/server.o
