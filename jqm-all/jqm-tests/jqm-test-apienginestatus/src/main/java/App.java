/**
 * Copyright © 2013 enioka. All rights reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.util.HashMap;
import java.util.Map;

import com.enioka.jqm.api.JobManager;

/**
 * Copyright © 2013 enioka. All rights reserved Authors: Marc-Antoine GOUILLART (marc-antoine.gouillart@enioka.com) Pierre COPPEE
 * (pierre.coppee@enioka.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

public class App implements Runnable
{
    JobManager jm;

    @Override
    public void run()
    {
        if (jm.parameters().containsKey("fail"))
        {
            throw new RuntimeException("was asked to fail");
        }
        if (jm.parameters().containsKey("succeed"))
        {
            return;
        }

        Map<String, String> prms = new HashMap<String, String>();
        prms.put("fail", "1");
        int failId = jm.enqueueSync(jm.applicationName(), jm.userName(), null, null, null, null, null, null, null, prms);

        prms = new HashMap<String, String>();
        prms.put("succeed", "1");
        int successId = jm.enqueueSync(jm.applicationName(), jm.userName(), null, null, null, null, null, null, null, prms);

        if (!jm.hasEnded(failId))
        {
            throw new RuntimeException("API issue");
        }
        if (!jm.hasFailed(failId))
        {
            throw new RuntimeException("API issue");
        }
        if (!jm.hasSucceeded(successId))
        {
            throw new RuntimeException("API issue");
        }
    }
}
