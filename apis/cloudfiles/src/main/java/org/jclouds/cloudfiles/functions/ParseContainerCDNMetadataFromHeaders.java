/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.cloudfiles.functions;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

import java.net.URI;
import java.util.List;

import org.jclouds.cloudfiles.domain.ContainerCDNMetadata;
import org.jclouds.cloudfiles.reference.CloudFilesHeaders;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.rest.InvocationContext;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

/**
 * This parses {@link AccountMetadata} from HTTP headers.
 */
public class ParseContainerCDNMetadataFromHeaders implements
         Function<HttpResponse, ContainerCDNMetadata>, InvocationContext<ParseContainerCDNMetadataFromHeaders> {

   private HttpRequest request;

   /**
    * parses the http response headers to create a new {@link ContainerCDNMetadata} object.
    */
   public ContainerCDNMetadata apply(final HttpResponse from) {
      String cdnEnabled = checkNotNull(from.getFirstHeaderOrNull(CloudFilesHeaders.CDN_ENABLED), CloudFilesHeaders.CDN_ENABLED);
      String cdnLogRetention = checkNotNull(from.getFirstHeaderOrNull(CloudFilesHeaders.CDN_LOG_RETENTION), CloudFilesHeaders.CDN_LOG_RETENTION);
      String cdnTTL = checkNotNull(from.getFirstHeaderOrNull(CloudFilesHeaders.CDN_TTL), CloudFilesHeaders.CDN_TTL);
      String cdnUri = checkNotNull(from.getFirstHeaderOrNull(CloudFilesHeaders.CDN_URI), CloudFilesHeaders.CDN_URI);
      String cdnSslUri = checkNotNull(from.getFirstHeaderOrNull(CloudFilesHeaders.CDN_SSL_URI), CloudFilesHeaders.CDN_SSL_URI);
      String cdnStreamingUri = checkNotNull(from.getFirstHeaderOrNull(CloudFilesHeaders.CDN_STREAMING_URI), CloudFilesHeaders.CDN_STREAMING_URI);
      String cdnIosUri = checkNotNull(from.getFirstHeaderOrNull(CloudFilesHeaders.CDN_IOS_URI), CloudFilesHeaders.CDN_IOS_URI);

      List<String> parts = newArrayList(Splitter.on('/').split(request.getEndpoint().getPath()));
      checkArgument(!parts.isEmpty(), "incorrect path: " + request.getEndpoint().getPath());

      return new ContainerCDNMetadata(
         Iterables.getLast(parts),
         Boolean.parseBoolean(cdnEnabled),
         Boolean.parseBoolean(cdnLogRetention),
         Long.parseLong(cdnTTL),
         URI.create(cdnUri),
         URI.create(cdnSslUri),
         URI.create(cdnStreamingUri),
         URI.create(cdnIosUri));
   }

   @Override
   public ParseContainerCDNMetadataFromHeaders setContext(HttpRequest request) {
      this.request = request;
      return this;
   }
}
