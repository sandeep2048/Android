/*
 * Copyright (c) 2021 DuckDuckGo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duckduckgo.mobile.android.vpn.pixels

import com.duckduckgo.app.global.DispatcherProvider
import com.duckduckgo.app.statistics.api.RefreshRetentionAtbPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.AppTpVpnFeature
import com.duckduckgo.mobile.android.vpn.VpnFeaturesRegistry
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ContributesMultibinding(AppScope::class)
class DeviceShieldRetentionPixelSender @Inject constructor(
    private val deviceShieldPixels: DeviceShieldPixels,
    private val vpnFeaturesRegistry: VpnFeaturesRegistry,
    private val coroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
) : RefreshRetentionAtbPlugin {

    override fun onSearchRetentionAtbRefreshed() {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (vpnFeaturesRegistry.isFeatureRunning(AppTpVpnFeature.APPTP_VPN)) {
                deviceShieldPixels.deviceShieldEnabledOnSearch()
            } else {
                deviceShieldPixels.deviceShieldDisabledOnSearch()
            }
        }
    }

    override fun onAppRetentionAtbRefreshed() {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (vpnFeaturesRegistry.isFeatureRunning(AppTpVpnFeature.APPTP_VPN)) {
                deviceShieldPixels.deviceShieldEnabledOnAppLaunch()
            } else {
                deviceShieldPixels.deviceShieldDisabledOnAppLaunch()
            }
        }
    }
}
