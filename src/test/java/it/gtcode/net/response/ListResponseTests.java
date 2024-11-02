package it.gtcode.net.response;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.MethodName.class)
class ListResponseTests {

    @Test
    void ListResponse() {
        try {

            var response = new ListResponse<String>();

            assertThat(response.getItems()).isEqualTo(Collections.emptyList());

        } catch (Exception e) {
            fail("ListResponse", e);
        }
    }

    @Test
    void asSuccess_list() {
        try {

            var expected = new ListResponse<String>();
            expected.setStatus(BasicStatus.SUCCESS);
            expected.setMessage(null);
            expected.setItems(List.of("A", "B", "C"));

            var response = new ListResponse<String>();

            response.asSuccess(expected.getItems());

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("asSuccess_list", e);
        }
    }

}