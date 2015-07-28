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

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.exoplatform.commons.api.notification.NotificationContext;
import org.exoplatform.commons.api.notification.model.ArgumentLiteral;
import org.exoplatform.commons.api.notification.model.MessageInfo;
import org.exoplatform.commons.api.notification.model.NotificationInfo;
import org.exoplatform.commons.api.notification.plugin.AbstractNotificationPlugin;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.manager.RelationshipManager;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvc@exoplatform.com
 * Jan 19, 2015  
 */
public class UpdateProfileBaseInfo_4_1_x_Plugin extends AbstractNotificationPlugin {
  public final static ArgumentLiteral<Profile> PROFILE = new ArgumentLiteral<Profile>(Profile.class, "profile");
  private static final Log LOG = ExoLogger.getLogger(UpdateProfileBaseInfo_4_1_x_Plugin.class);
  public final static String ID = "UpdateProfileBaseInfo_4_1_x_Plugin";
  public final static String SUBJECT = " updated the basic information.";

  public UpdateProfileBaseInfo_4_1_x_Plugin(InitParams initParams) {
    super(initParams);
  }
  
  @Override
  protected MessageInfo makeMessage(NotificationContext ctx) {
    MessageInfo messageInfo = new MessageInfo();
    NotificationInfo notification = ctx.getNotificationInfo();
    
    IdentityManager identityManager = CommonsUtils.getService(IdentityManager.class);
    Identity identity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, notification.getFrom(), false);
    Profile profile = identityManager.getProfile(identity);
    String subject = profile.getFullName() + SUBJECT;
    String body = profile.getFullName() + SUBJECT + "<br/>";
    
    return messageInfo.from(notification.getFrom()).subject(subject).body(body).end();
  }

  @Override
  protected boolean makeDigest(NotificationContext ctx, Writer writer) {
    return false;
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
                           .setTitle(updatedIdentity.getProfile().getFullName() + " updated the basic info.<br>")
                           .key(getId());
  }

}
