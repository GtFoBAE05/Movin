package org.imannuel.movin.bankservice.service.impl;

import com.imannuel.movin.commonservice.utility.ValidationUtility;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.movin.bankservice.dto.mapper.BankMapper;
import org.imannuel.movin.bankservice.dto.request.BankRequest;
import org.imannuel.movin.bankservice.dto.request.PaymentOptionsRequest;
import org.imannuel.movin.bankservice.dto.response.BankCustomerResponse;
import org.imannuel.movin.bankservice.dto.response.BankResponse;
import org.imannuel.movin.bankservice.entity.Bank;
import org.imannuel.movin.bankservice.repository.BankRepository;
import org.imannuel.movin.bankservice.seed.BankSeedData;
import org.imannuel.movin.bankservice.service.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;
    private final ValidationUtility validationUtility;

    @PostConstruct
    @Transactional(rollbackFor = Exception.class)
    void init() {
        log.info("Starting bank initialization process");

        List<Bank> bankSeedData = BankSeedData.getBankSeedData();
        bankSeedData.forEach(bank -> bankRepository.findByCode(bank.getCode()).ifPresentOrElse(
                existing -> log.debug("Bank {} already exists, skipping.", existing.getCode()),
                () -> {
                    log.debug("Creating bank: {}", bank.getCode());
                    bankRepository.save(bank);
                    log.info("Bank {} created successfully", bank.getCode());
                }
        ));

        log.info("Banks initialization completed");
    }

    @Override
    @Transactional(readOnly = true)
    public Bank findBank(String code) {
        log.debug("Searching for bank with code: {}", code);
        return bankRepository.findByCode(code)
                .orElseThrow(() -> {
                    log.warn("Bank not found with code: {}", code);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Bank with code " + code + " not found");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Bank findBankById(String id) {
        log.debug("Searching for bank with id: {}", id);
        return bankRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Bank not found with id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Bank with id " + id + " not found");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankCustomerResponse> getAllBanksForCustomer() {
        log.debug("Fetching all active banks for customer");
        List<BankCustomerResponse> responses = bankRepository.findAllByStatusTrue().stream()
                .map(BankMapper::toBankCustomerResponse)
                .toList();

        log.info("Retrieved {} active banks for customer", responses.size());
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankCustomerResponse> getAvailablePaymentOptions(PaymentOptionsRequest request) {
        log.debug("Fetching available payment options for request: {}", request);

        validationUtility.validate(request);

        findBank(request.getTargetBank());

        List<BankCustomerResponse> responses = bankRepository.findAllByStatusTrueAndCodeIsNot(request.getTargetBank())
                .stream()
                .map(BankMapper::toBankCustomerResponse)
                .toList();

        log.info("Retrieved {} available payment options", responses.size());
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankResponse> getAllBanksForAdmin() {
        log.debug("Fetching all banks for admin");
        List<BankResponse> responses = bankRepository.findAll().stream()
                .map(BankMapper::toBankResponse)
                .toList();

        log.info("Retrieved {} total banks", responses.size());
        return responses;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BankResponse createBank(BankRequest bankRequest) {
        log.info("Attempting to create new bank: {}", bankRequest.getCode());

        validationUtility.validate(bankRequest);

        if (bankRepository.existsByCode(bankRequest.getCode())) {
            log.warn("Bank already exists with code: {}", bankRequest.getCode());
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Bank with code " + bankRequest.getCode() + " already exists");
        }

        Bank savedBank = bankRepository.saveAndFlush(BankMapper.toBank(bankRequest));

        log.info("Successfully created bank: {}", savedBank.getCode());
        return BankMapper.toBankResponse(savedBank);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BankResponse updateBank(String code, BankRequest bankRequest) {
        log.info("Attempting to update bank with code: {}", code);

        validationUtility.validate(bankRequest);

        Bank existingBank = findBank(code);

        existingBank.setCode(bankRequest.getCode());
        existingBank.setName(bankRequest.getName());
        existingBank.setFee(bankRequest.getFee());
        existingBank.setImage(bankRequest.getImage());
        existingBank.setStatus(bankRequest.isStatus());

        Bank updatedBank = bankRepository.saveAndFlush(existingBank);

        log.info("Successfully updated bank: {}", updatedBank.getCode());
        return BankMapper.toBankResponse(updatedBank);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkBankExists(String code) {
        log.debug("Checking bank existence for code: {}", code);
        return bankRepository.existsByCode(code);
    }
}
