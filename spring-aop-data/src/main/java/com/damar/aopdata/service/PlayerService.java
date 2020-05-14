package com.damar.aopdata.service;


import com.damar.aopdata.exception.PlayerNotFoundException;
import com.damar.aopdata.model.Player;

import java.util.List;

public interface PlayerService {

    Player getById(Long id) throws PlayerNotFoundException;

    List<Player> getAll(Integer age, String name, Boolean isActive);

    Player updatePlayer(Player player) throws PlayerNotFoundException;

    Player savePlayer(Player player);

    void deletePlayer(Long id) throws PlayerNotFoundException;

    List<Player> getAllByMinMaxAge(Integer minAge, Integer maxAge, Boolean equal);

}
