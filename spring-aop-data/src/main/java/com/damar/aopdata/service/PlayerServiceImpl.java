package com.damar.aopdata.service;


import com.damar.aopdata.exception.PlayerNotFoundException;
import com.damar.aopdata.model.Player;
import com.damar.aopdata.repository.PlayerRepository;
import com.damar.aopdata.repository.entity.PlayerEntity;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private DozerBeanMapper mapper;

    private PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(DozerBeanMapper mapper, PlayerRepository playerRepository) {
        this.mapper = mapper;
        this.playerRepository = playerRepository;
    }

    @Override
    public Player getById(Long id) throws PlayerNotFoundException {
        PlayerEntity playerEntity = playerRepository.findById(id).orElse(null);
        if (Objects.isNull(playerEntity))
            throw new PlayerNotFoundException();
        return mapper.map(playerEntity, Player.class);
    }

    @Override
    public List<Player> getAll(Integer age, String name, String surname, Boolean isActive) {
        List<PlayerEntity> playerEntityList;

        if (Objects.nonNull(isActive)) {
            playerEntityList = findAllByActiveNotNull(age, name, surname, isActive);
        } else {
            playerEntityList = findAllByActiveNull(age, name, surname);
        }

        return playerEntityList.stream().map(entity -> mapper.map(entity, Player.class))
                .collect(Collectors.toList());
    }

    private List<PlayerEntity> findAllByActiveNotNull(Integer age, String name, String surname, Boolean isActive) {
        if (Objects.nonNull(age)) {
            return findByAgeNotNullAndActiveNotNull(isActive, age, name, surname);
        } else {
            return findByAgeNullAndActiveNotNull(isActive, name, surname);
        }
    }

    private List<PlayerEntity> findAllByActiveNull(Integer age, String name, String surname) {
        if (Objects.nonNull(age)) {
            return findByAgeNotNullAndActiveNull(age, name, surname);
        } else {
            return findByAgeNullAndActiveNull(name, surname);
        }
    }

    private List<PlayerEntity> findByAgeNotNullAndActiveNull(Integer age, String name, String surname) {
        if (StringUtils.isNotBlank(name)) {
            if (StringUtils.isNotBlank(surname))
                return playerRepository.
                        findByPlayerDetail_AgeAndNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(age, name, surname);
            else
                return playerRepository.findByPlayerDetail_AgeAndNameContainingIgnoreCase(age, name);
        } else {
            if (StringUtils.isNotBlank(surname))
                return playerRepository.findByPlayerDetail_AgeAndSurnameContainingIgnoreCase(age, surname);
            else
                return playerRepository.findByPlayerDetail_Age(age);
        }
    }

    private List<PlayerEntity> findByAgeNullAndActiveNull(String name, String surname) {
        if (StringUtils.isNotBlank(name)) {
            if (StringUtils.isNotBlank(surname))
                return playerRepository.findByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(name, surname);
            else
                return playerRepository.findByNameContainingIgnoreCase(name);
        } else {
            if (StringUtils.isNotBlank(surname))
                return playerRepository.findBySurnameContainingIgnoreCase(surname);
            else
                return playerRepository.findAll();
        }
    }

    private List<PlayerEntity> findByAgeNotNullAndActiveNotNull(Boolean isActive, Integer age, String name, String surname) {
        if (StringUtils.isNotBlank(name)) {
            if (StringUtils.isNotBlank(surname))
                return playerRepository.
                        findByActiveAndPlayerDetail_AgeAndNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(isActive, age, name, surname);
            else
                return playerRepository.
                        findByActiveAndPlayerDetail_AgeAndNameContainingIgnoreCase(isActive, age, name);
        } else {
            if (StringUtils.isNotBlank(surname))
                return playerRepository.findByActiveAndPlayerDetail_AgeAndSurnameContainingIgnoreCase(isActive, age, surname);
            else
                return playerRepository.findByActiveAndPlayerDetail_Age(isActive, age);
        }
    }

    private List<PlayerEntity> findByAgeNullAndActiveNotNull(Boolean isActive, String name, String surname) {
        if (StringUtils.isNotBlank(name)) {
            if (StringUtils.isNotBlank(surname))
                return playerRepository.
                        findByActiveAndNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(isActive, name, surname);
            else
                return playerRepository.findByActiveAndNameContainingIgnoreCase(isActive, name);
        } else {
            if (StringUtils.isNotBlank(surname)) {
                if (BooleanUtils.isTrue(isActive))
                    return playerRepository.findByActiveTrueAndSurnameContainingIgnoreCase(surname);
                else
                    return playerRepository.findByActiveFalseAndSurnameContainingIgnoreCase(surname);
            } else {
                return findByActive(isActive);
            }
        }
    }

    private List<PlayerEntity> findByActive(Boolean isActive) {
        if (BooleanUtils.isTrue(isActive))
            return playerRepository.findByActiveTrue();
        else
            return playerRepository.findByActiveFalse();
    }

    @Override
    public Player updatePlayer(Player player) throws PlayerNotFoundException {
        if (BooleanUtils.isFalse(checkIfPlayerExists(player.getPlayerId())))
            throw new PlayerNotFoundException();
        PlayerEntity toUpdate = playerRepository.save(mapper.map(player, PlayerEntity.class));
        return mapper.map(toUpdate, Player.class);
    }

    @Override
    public Player savePlayer(Player player) {
        PlayerEntity newEntity = playerRepository.save(mapper.map(player, PlayerEntity.class));
        return mapper.map(newEntity, Player.class);
    }

    @Override
    public void deletePlayer(Long id) throws PlayerNotFoundException {
        PlayerEntity toDelete = playerRepository.findById(id).orElse(null);
        if (Objects.isNull(toDelete))
            throw new PlayerNotFoundException();
        playerRepository.delete(toDelete);
    }

    @Override
    public List<Player> getAllByMinMaxAge(Integer minAge, Integer maxAge, Boolean equal) {
        List<PlayerEntity> playerEntityList;

        if (BooleanUtils.isTrue(equal))
            playerEntityList = playerRepository.findByPlayerDetail_AgeGreaterThanEqualAndPlayerDetail_AgeLessThanEqual(minAge, maxAge);
        else
            playerEntityList = playerRepository.findByPlayerDetail_AgeGreaterThanAndPlayerDetail_AgeLessThan(minAge, maxAge);

        return playerEntityList.stream().map(entity -> mapper.map(entity, Player.class))
                .collect(Collectors.toList());
    }

    private boolean checkIfPlayerExists(Long playerId) {
        return playerRepository.existsById(playerId);
    }

}
