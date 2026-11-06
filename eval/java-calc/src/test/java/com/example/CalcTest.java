package com.example;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class CalcTest {
    @Test void add(){ assertEquals(5, new Calc().add(2,3)); }
}
