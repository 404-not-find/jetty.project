//
// ========================================================================
// Copyright (c) 1995-2020 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under
// the terms of the Eclipse Public License 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0
//
// This Source Code may also be made available under the following
// Secondary Licenses when the conditions for such availability set
// forth in the Eclipse Public License, v. 2.0 are satisfied:
// the Apache License v2.0 which is available at
// https://www.apache.org/licenses/LICENSE-2.0
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.websocket.client.impl;

import java.net.HttpCookie;
import java.net.SocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import org.eclipse.jetty.websocket.core.client.ClientUpgradeRequest;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Representing the Jetty {@link org.eclipse.jetty.client.HttpClient}'s {@link org.eclipse.jetty.client.HttpRequest}
 * in the {@link UpgradeRequest} interface.
 */
public class DelegatedJettyClientUpgradeRequest implements UpgradeRequest
{
    private final ClientUpgradeRequest delegate;
    private SocketAddress localSocketAddress;
    private SocketAddress remoteSocketAddress;

    public DelegatedJettyClientUpgradeRequest(ClientUpgradeRequest delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public List<HttpCookie> getCookies()
    {
        return delegate.getCookies();
    }

    @Override
    public String getHeader(String name)
    {
        return delegate.getHeaders().get(name);
    }

    @Override
    public int getHeaderInt(String name)
    {
        HttpField field = delegate.getHeaders().getField(name);
        if (field == null)
            return -1;
        return field.getIntValue();
    }

    @Override
    public List<String> getHeaders(String name)
    {
        return delegate.getHeaders().getValuesList(name);
    }

    @Override
    public Map<String, List<String>> getHeaders()
    {
        return null;
    }

    @Override
    public String getHost()
    {
        return delegate.getHost();
    }

    @Override
    public String getHttpVersion()
    {
        return delegate.getVersion().toString();
    }

    public void configure(EndPoint endpoint)
    {
        this.localSocketAddress = endpoint.getLocalAddress();
        this.remoteSocketAddress = endpoint.getRemoteAddress();
    }

    @Override
    public String getMethod()
    {
        return delegate.getMethod();
    }

    @Override
    public String getOrigin()
    {
        return delegate.getHeaders().get(HttpHeader.ORIGIN);
    }

    @Override
    public Map<String, List<String>> getParameterMap()
    {
        if (getQueryString() == null)
            return Collections.emptyMap();

        MultiMap<String> params = new MultiMap<>();
        UrlEncoded.decodeTo(getQueryString(), params, UTF_8);
        return params;
    }

    @Override
    public String getProtocolVersion()
    {
        return delegate.getHeaders().get(HttpHeader.SEC_WEBSOCKET_VERSION);
    }

    @Override
    public String getQueryString()
    {
        return delegate.getQuery();
    }

    @Override
    public URI getRequestURI()
    {
        return delegate.getURI();
    }

    @Override
    public List<String> getSubProtocols()
    {
        return delegate.getHeaders().getValuesList(HttpHeader.SEC_WEBSOCKET_SUBPROTOCOL);
    }

    @Override
    public boolean hasSubProtocol(String test)
    {
        HttpField field = delegate.getHeaders().getField(HttpHeader.SEC_WEBSOCKET_SUBPROTOCOL);
        if (field == null)
            return false;
        return field.contains(test);
    }

    @Override
    public boolean isSecure()
    {
        // TODO: figure out how to obtain from HttpClient's HttpRequest
        return false;
    }

    @Override
    public void addExtensions(org.eclipse.jetty.websocket.api.extensions.ExtensionConfig... configs)
    {
        // TODO
    }

    @Override
    public void addExtensions(String... configs)
    {
        // TODO
    }

    @Override
    public Object getSession()
    {
        return null;
    }

    @Override
    public Principal getUserPrincipal()
    {
        return null;
    }

    @Override
    public void setCookies(List<HttpCookie> cookies)
    {
        // TODO
    }

    @Override
    public List<ExtensionConfig> getExtensions()
    {
        List<String> rawExtensions = delegate.getHeaders().getValuesList(HttpHeader.SEC_WEBSOCKET_EXTENSIONS);
        if (rawExtensions == null || rawExtensions.isEmpty())
            return Collections.emptyList();

        return rawExtensions.stream().map((parameterizedName) -> ExtensionConfig.parse(parameterizedName)).collect(Collectors.toList());
    }

    @Override
    public void setExtensions(List<org.eclipse.jetty.websocket.api.extensions.ExtensionConfig> configs)
    {
        // TODO
    }

    @Override
    public void setHeader(String name, List<String> values)
    {
        // TODO
    }

    @Override
    public void setHeader(String name, String value)
    {
        // TODO
    }

    @Override
    public void setHeaders(Map<String, List<String>> headers)
    {
        // TODO
    }

    @Override
    public void setSession(Object session)
    {
        // TODO
    }

    @Override
    public void setSubProtocols(List<String> protocols)
    {
        // TODO
    }

    @Override
    public void setSubProtocols(String... protocols)
    {
        // TODO
    }
}
