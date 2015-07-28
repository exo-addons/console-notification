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
package org.exoplatform.social.notification;

import org.exoplatform.commons.api.notification.NotificationContext;
import org.exoplatform.commons.api.notification.model.PluginKey;
import org.exoplatform.commons.notification.impl.NotificationContextImpl;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.profile.ProfileLifeCycleEvent;
import org.exoplatform.social.core.profile.ProfileListenerPlugin;
import org.exoplatform.social.notification.plugin.UpdateProfileBaseInfo_4_1_x_Plugin;
import org.exoplatform.social.notification.plugin.UpdateProfileHeaderPlugin;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvc@exoplatform.com
 * Jan 19, 2015  
 */
public class SocialProfileListener extends ProfileListenerPlugin {

  @Override
  public void avatarUpdated(ProfileLifeCycleEvent event) {
    Profile profile = event.getProfile();
    NotificationContext ctx = NotificationContextImpl.cloneInstance().append(UpdateProfileHeaderPlugin.PROFILE, profile);
    ctx.getNotificationExecutor().with(ctx.makeCommand(PluginKey.key(UpdateProfileHeaderPlugin.ID))).execute(ctx);
  }

  @Override
  public void basicInfoUpdated(ProfileLifeCycleEvent event) {
    Profile profile = event.getProfile();
    NotificationContext ctx = NotificationContextImpl.cloneInstance().append(UpdateProfileBaseInfo_4_1_x_Plugin.PROFILE, profile);
    ctx.getNotificationExecutor().with(ctx.makeCommand(PluginKey.key(UpdateProfileBaseInfo_4_1_x_Plugin.ID))).execute(ctx);
  }

  @Override
  public void contactSectionUpdated(ProfileLifeCycleEvent event) {
    Profile profile = event.getProfile();
    NotificationContext ctx = NotificationContextImpl.cloneInstance().append(UpdateProfileHeaderPlugin.PROFILE, profile);
    ctx.getNotificationExecutor().with(ctx.makeCommand(PluginKey.key(UpdateProfileHeaderPlugin.ID))).execute(ctx);
  }

  @Override
  public void experienceSectionUpdated(ProfileLifeCycleEvent event) {}

  @Override
  public void headerSectionUpdated(ProfileLifeCycleEvent event) {
    Profile profile = event.getProfile();
    NotificationContext ctx = NotificationContextImpl.cloneInstance().append(UpdateProfileHeaderPlugin.PROFILE, profile);
    ctx.getNotificationExecutor().with(ctx.makeCommand(PluginKey.key(UpdateProfileHeaderPlugin.ID))).execute(ctx);
  }

  @Override
  public void createProfile(ProfileLifeCycleEvent event) {
   
  }

}

