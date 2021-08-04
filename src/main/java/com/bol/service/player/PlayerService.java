package com.bol.service.player;

import com.bol.model.entity.PlayerEntity;

/**
 * A <i>PlayerService</i>. This interface has responsibility to create and
 * control the game and movements.<p>
 * The <tt>GameService</tt> interface provides two methods for creating and fetching the player
 * the player service implemented by <tt>PlayerServiceImp</tt> class.<p>
 *
 * @author Omid Rostami
 */

public interface PlayerService {

    /**
     * Returns the created player's id.
     *
     * @param playerDto contains detail of player whom to be created
     * @return the player's id just created.
     */

    Long createPlayer(PlayerEntity playerEntity);


    /**
     * Returns the details of player fetched
     *
     * @param playerId the Player of the game who created it for the first time
     * @return the player entity which fetched.
     */


    PlayerEntity getById(Long playerId);

}
