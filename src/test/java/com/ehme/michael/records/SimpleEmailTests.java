package com.ehme.michael.records;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleEmailTests {

    @Test
    public void testSimpleEmail(){
        SimpleEmail simpleEmail = new SimpleEmail("FROM", "SUBJECT", "TEXT");
        assertThat(simpleEmail.from()).isEqualTo("FROM");
        assertThat(simpleEmail.subject()).isEqualTo("SUBJECT");
        assertThat(simpleEmail.text()).isEqualTo("TEXT");
    }
}
