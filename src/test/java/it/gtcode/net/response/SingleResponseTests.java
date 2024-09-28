package it.gtcode.net.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SingleResponseTests {

    @Test
    void SingleResponse() {
        try {

            var expected = new SingleResponse<String>();
            expected.setStatus(BasicStatus.UNKNOWN);
            expected.setMessage(BasicStatus.UNKNOWN.getMessage());
            expected.setItem(null);

            var response = new SingleResponse<String>();

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("SingleResponse", e);
        }
    }

    @Test
    void getItem() {
        try {

            var response = new SingleResponse<String>();
            response.setStatus(BasicStatus.SUCCESS);
            response.setMessage(null);
            response.setItem("ITEM");

            assertThat(response.getItem())
                    .isNotEmpty()
                    .contains("ITEM");

        } catch (Exception e) {
            fail("getItem", e);
        }
    }

    @Test
    void asSuccess_item() {
        try {

            var expected = new SingleResponse<String>();
            expected.setStatus(BasicStatus.SUCCESS);
            expected.setMessage(null);
            expected.setItem("ITEM");

            var response = new SingleResponse<String>();

            response.asSuccessful("ITEM");

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("asSuccess_item", e);
        }
    }

}