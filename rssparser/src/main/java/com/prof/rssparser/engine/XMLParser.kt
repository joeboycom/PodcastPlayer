/*
*   Copyright 2016 Marco Gomiero
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*
*/

package com.prof.rssparser.engine

import com.prof.rssparser.Channel
import com.prof.rssparser.core.CoreXMLParser
import java.util.concurrent.Callable

internal class XMLParser(var xml: String) : Callable<Channel> {

    @Throws(Exception::class)
    override fun call(): Channel {
        return CoreXMLParser.parseXML(xml)
    }
}
