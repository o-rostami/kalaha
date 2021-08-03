package com.bol.service.game;


import com.bol.model.entity.GameEntity;
import com.bol.model.entity.PlayerEntity;
import com.bol.model.enums.DifficultyLevel;

/**
 * A <i>GameService</i>. This interface has responsibility to create and
 * control the game and movements.<p>
 * The <tt>GameService</tt> interface provides five methods for controlling
 * the game implemented by <tt>GameServiceImp</tt> class.<p>
 *
 * @author Omid Rostami
 */
public interface GameService {

    /**
     * Returns the created game based on the player and specified difficulty level.
     *
     * @param player the Player of the game who created it for the first time
     * @param difficultyLevel the Player of the game who created it for the first time
     * @return the game entity just created.
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    GameEntity createGame(PlayerEntity player, DifficultyLevel difficultyLevel);

    GameEntity connect(PlayerEntity player, Long gameId);

    GameEntity connectToRandomGame(PlayerEntity player);

    GameEntity getGameById(Long gameId);

    GameEntity gamePlay(Long gameId, Integer pitId);

}
