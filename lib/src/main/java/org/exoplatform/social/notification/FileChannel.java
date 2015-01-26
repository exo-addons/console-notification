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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.exoplatform.commons.api.notification.NotificationContext;
import org.exoplatform.commons.api.notification.channel.AbstractChannel;
import org.exoplatform.commons.api.notification.channel.template.AbstractTemplateBuilder;
import org.exoplatform.commons.api.notification.channel.template.TemplateProvider;
import org.exoplatform.commons.api.notification.model.ChannelKey;
import org.exoplatform.commons.api.notification.model.MessageInfo;
import org.exoplatform.commons.api.notification.model.NotificationInfo;
import org.exoplatform.commons.api.notification.model.PluginKey;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvc@exoplatform.com
 * Jan 19, 2015  
 */
public class FileChannel extends AbstractChannel {
  /** logger */
  private static final Log LOG = ExoLogger.getLogger(FileChannel.class);
  /** */
  private final static String ID = "FILE_CHANNEL";
  /** */
  private final ChannelKey key = ChannelKey.key(ID);
  
  
  
  public FileChannel() {
    super();
  }
  
  @Override
  public String getId() {
    return ID;
  }
  
  @Override
  public ChannelKey getKey() {
    return key;
  }
  
  @Override
  public void dispatch(NotificationContext ctx, String userId) {
    LOG.info(String.format("FILE:: %s will be received the message from pluginId: %s", userId, ctx.getNotificationInfo().getKey().getId()));
    AbstractTemplateBuilder templateBuilder = getTemplateBuilderInChannel(ctx.getNotificationInfo().getKey());
    MessageInfo msg = templateBuilder.buildMessage(ctx);
    
    File file = new File("notifications-log.txt");
    try {
      // if file doesn't exists, then create it
      if (!file.exists()) {
        file.createNewFile();
      }
      FileWriter fileWritter = new FileWriter(file.getName(), true);
      BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
      bufferWritter.write(msg.toString());
      bufferWritter.close();
    } catch (IOException iex) {
      LOG.error(iex.getMessage(), iex);
    }
  }
  
  @Override
  public void registerTemplateProvider(TemplateProvider provider) {}
  
  @Override
  protected AbstractTemplateBuilder getTemplateBuilderInChannel(PluginKey key) {
    return new AbstractTemplateBuilder() {
      @Override
      protected MessageInfo makeMessage(NotificationContext ctx) {
        NotificationInfo notification = ctx.getNotificationInfo();
        MessageInfo messageInfo = new MessageInfo();
        return messageInfo.from(notification.getFrom())
                          .to(notification.getTo())
                          .body(notification.getKey().getId() + " raised notification: " + notification.getTitle())
                          .end();
      }

      @Override
      protected boolean makeDigest(NotificationContext ctx, Writer writer) {
        return false;
      }
    };
  }
}
