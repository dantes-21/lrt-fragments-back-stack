package com.dantes.lrtbackstack.exceptions

class NoSuchStackException(stack: String?)
    : RuntimeException("Stack with name - $stack not found!")