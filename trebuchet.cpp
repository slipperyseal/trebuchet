#include <stdio.h>
#include <stdlib.h>
#include <new>
#include "trebuchet.h"

Stack primaryStack = Stack(0xffff);

void * operator new(size_t size) throw(std::bad_alloc) {
    return primaryStack.push(size);
}

void * operator new(size_t size, Stack * stack) throw(std::bad_alloc) {
    return stack->push(size);
}

void operator delete(void *p) throw() {
    printf("delete\n");
}

Stack::Stack(int capasity) {
    this->capasity = capasity;
    this->offset = this->capasity;
    this->data = (char*)calloc(this->capasity,1);
}

void* Stack::push(int size) {
    // round to pointer size boundary
    offset -= ~(sizeof(void*)-1) & (size + sizeof(void*)-1);
    if (offset < 0) {
        std::bad_alloc exception;
        throw exception;
    }
    printf("push %d %d\n", size, offset);
    return &data[offset];
}

// java arrays are allocated as the length of the array followed by the data but are returned as a pointer to the start of the data
// [int:len][...data...]
//          ^ returned pointer

void* newObjectArray(int len) {
    int * array = (int *)new char[ (len*sizeof(void*))+sizeof(int) ];
    array[0] = len;
    return (void*)&array[1];
}

int* newIntArray(int len) {
    int * array = (int *)new char[ (len*sizeof(int))+sizeof(int) ];
    array[0] = len;
    return (int*)&array[1];
}

short* newCharArray(int len) {
    int * array = (int *)new char[ (len*sizeof(short))+sizeof(int) ];
    array[0] = len;
    return (short*)&array[1];
}

char* newByteArray(int len) {
    int * array = (int *)new char[ (len*sizeof(char))+sizeof(int) ];
    array[0] = len;
    return (char*)&array[1];
}

int getArrayLen(void * array) {
    return ((int*)array)[-1];
}