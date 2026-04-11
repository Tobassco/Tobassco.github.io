CC = gcc
CFLAGS = -Wall -g -std=c99
SRCS = main.c process.c queue.c scheduler.c output.c stats.c 
OBJS = $(SRCS:.c=.o)
TARGET = schedule
all: $(TARGET)
$(TARGET):$(OBJS)
	$(CC) $(CFLAGS) -o $@ $^ $(LDFLAGS)
%.o:%.c
	$((CC)) $(CFLAGS) -c $< -o $@
clean:
	rm -f $(OBJS) $(TARGET)
.PHONY: all clean
