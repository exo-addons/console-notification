/*
 * Copyright (C) 2003-2015 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.social.notification.plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.exoplatform.commons.api.notification.NotificationContext;
import org.exoplatform.commons.api.notification.model.ArgumentLiteral;
import org.exoplatform.commons.api.notification.model.NotificationInfo;
import org.exoplatform.commons.api.notification.plugin.BaseNotificationPlugin;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.manager.RelationshipManager;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jan 21, 2015  
 */
public class UpdateProfileHeaderPlugin extends BaseNotificationPlugin {
  public final static ArgumentLiteral<Profile> PROFILE = new ArgumentLiteral<Profile>(Profile.class, "profile");
  private static final Log LOG = ExoLogger.getLogger(UpdateProfileHeaderPlugin.class);
  public final static String ID = "UpdateProfileHeaderPlugin";

  public UpdateProfileHeaderPlugin(InitParams initParams) {
    super(initParams);
  }

  @Override
  public String getId() {
    return ID;
  }

  @Override
  public boolean isValid(NotificationContext ctx) {
    return true;
  }

  @Override
  protected NotificationInfo makeNotification(NotificationContext ctx) {
    Profile profile = ctx.value(PROFILE);
    Set<String> receivers = new HashSet<String>();
    
    RelationshipManager relationshipManager = CommonsUtils.getService(RelationshipManager.class);
    Identity updatedIdentity = profile.getIdentity();
    ListAccess<Identity> listAccess = relationshipManager.getConnections(updatedIdentity);
    try {
      Identity[] relationships =  relationshipManager.getConnections(updatedIdentity).load(0, listAccess.getSize());
      for(Identity i : relationships) {
        receivers.add(i.getRemoteId());
      }
    } catch (Exception ex) {
      LOG.error(ex.getMessage(), ex);
    }
    
    return NotificationInfo.instance()
                           .setFrom(updatedIdentity.getRemoteId())
                           .to(new ArrayList<String>(receivers))
                           .key(getId());
  }

}