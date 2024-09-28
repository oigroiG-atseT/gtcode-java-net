package it.gtcode.net.response;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class PaginatedResponseTests {

    @Test
    void PaginatedResponse() {
        try {

            var expected = new PaginatedResponse<String>();
            expected.setStatus(BasicStatus.UNKNOWN);
            expected.setMessage(BasicStatus.UNKNOWN.getMessage());
            expected.setItems(Collections.emptyList());
            expected.setTotalCount(0);

            var response = new PaginatedResponse<String>();

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("PaginatedResponse", e);
        }
    }

    @Test
    void asSuccess_paginatedList() {
        try {

            var expected = new PaginatedResponse<String>();
            expected.setStatus(BasicStatus.SUCCESS);
            expected.setMessage(BasicStatus.UNKNOWN.getMessage());
            expected.setItems(List.of("A", "B", "C"));
            expected.setTotalCount(6);

            var response = new PaginatedResponse<String>();

            response.asSuccess(expected.getItems(), expected.getTotalCount());

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("asSuccess_list", e);
        }
    }

}