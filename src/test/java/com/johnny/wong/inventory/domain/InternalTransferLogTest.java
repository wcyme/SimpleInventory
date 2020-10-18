package com.johnny.wong.inventory.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.johnny.wong.inventory.web.rest.TestUtil;

public class InternalTransferLogTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InternalTransferLog.class);
        InternalTransferLog internalTransferLog1 = new InternalTransferLog();
        internalTransferLog1.setId(1L);
        InternalTransferLog internalTransferLog2 = new InternalTransferLog();
        internalTransferLog2.setId(internalTransferLog1.getId());
        assertThat(internalTransferLog1).isEqualTo(internalTransferLog2);
        internalTransferLog2.setId(2L);
        assertThat(internalTransferLog1).isNotEqualTo(internalTransferLog2);
        internalTransferLog1.setId(null);
        assertThat(internalTransferLog1).isNotEqualTo(internalTransferLog2);
    }
}
