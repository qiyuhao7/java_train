package com.training.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

/**
 * AccountService 单元测试
 * 演示：JUnit 5 + Mockito
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("账户服务测试")
class AccountServiceTest {

    @Mock
    private AccountRepository repo;

    @Mock
    private NotificationClient notifier;

    @InjectMocks
    private AccountService accountService;

    private Account fromAccount;
    private Account toAccount;

    @BeforeEach
    void setUp() {
        fromAccount = new Account(1L, 100L, new BigDecimal("1000"));
        toAccount = new Account(2L, 200L, new BigDecimal("500"));
    }

    // ===== 正常场景 =====

    @Test
    @DisplayName("正常转账：余额正确扣减和增加")
    void should_transferSuccessfully_when_balanceSufficient() {
        // Given
        when(repo.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(repo.findById(2L)).thenReturn(Optional.of(toAccount));
        when(repo.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Account result = accountService.transfer(1L, 2L, new BigDecimal("300"));

        // Then
        assertEquals(new BigDecimal("700"), result.getBalance());
        assertEquals(new BigDecimal("800"), toAccount.getBalance());

        // 验证交互
        verify(repo, times(2)).save(any(Account.class));
        verify(notifier).send(eq(200L), contains("300"));
    }

    @Test
    @DisplayName("转账全部余额：余额变为0")
    void should_transferAllBalance() {
        when(repo.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(repo.findById(2L)).thenReturn(Optional.of(toAccount));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        accountService.transfer(1L, 2L, new BigDecimal("1000"));

        assertEquals(BigDecimal.ZERO.compareTo(fromAccount.getBalance()), 0);
        assertEquals(new BigDecimal("1500"), toAccount.getBalance());
    }

    // ===== 异常场景 =====

    @Test
    @DisplayName("金额为null：抛出 IllegalArgumentException")
    void should_throwException_when_amountNull() {
        assertThrows(IllegalArgumentException.class,
            () -> accountService.transfer(1L, 2L, null));
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("金额<=0：抛出 IllegalArgumentException")
    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "-100.5"})
    void should_throwException_when_amountNotPositive(String amountStr) {
        BigDecimal amount = new BigDecimal(amountStr);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> accountService.transfer(1L, 2L, amount));
        assertTrue(ex.getMessage().contains("大于0"));
    }

    @Test
    @DisplayName("转账给自己：抛出异常")
    void should_throwException_when_transferToSelf() {
        assertThrows(IllegalArgumentException.class,
            () -> accountService.transfer(1L, 1L, new BigDecimal("100")));
    }

    @Test
    @DisplayName("转出账户不存在：抛出 NotFoundException")
    void should_throwNotFound_when_fromAccountMissing() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
            () -> accountService.transfer(1L, 2L, new BigDecimal("100")));
        assertTrue(ex.getMessage().contains("1"));
    }

    @Test
    @DisplayName("余额不足：抛出 BusinessException")
    void should_throwBusiness_when_balanceInsufficient() {
        when(repo.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(repo.findById(2L)).thenReturn(Optional.of(toAccount));

        assertThrows(BusinessException.class,
            () -> accountService.transfer(1L, 2L, new BigDecimal("9999")));

        // 验证没有保存操作
        verify(repo, never()).save(any());
        verify(notifier, never()).send(anyLong(), anyString());
    }

    // ===== 查询余额 =====

    @Test
    @DisplayName("查询存在的账户余额")
    void should_returnBalance_when_accountExists() {
        when(repo.findById(1L)).thenReturn(Optional.of(fromAccount));
        assertEquals(new BigDecimal("1000"), accountService.getBalance(1L));
    }

    @Test
    @DisplayName("查询不存在的账户：返回0")
    void should_returnZero_when_accountNotExists() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertEquals(BigDecimal.ZERO, accountService.getBalance(99L));
    }
}
