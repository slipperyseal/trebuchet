
#ifndef TREBUCHET_H
#define TREBUCHET_H

#include <new>

class Stack {
public:
    Stack(int capasity);
    void * push(int size);
private:
    char * data;
    int capasity;
    int offset;
};

void * operator new(size_t size, Stack * stack) throw(std::bad_alloc);

int* newIntArray(int len);
short* newCharArray(int len);
char* newByteArray(int len);
int getArrayLen(void * array);

#endif
