/*
 * (C) Copyright 2014 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Nuxeo
 */

package org.nuxeo.elasticsearch.test;

import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.elasticsearch.ElasticSearchAdmin;
import org.nuxeo.elasticsearch.ElasticSearchService;
import org.nuxeo.elasticsearch.NuxeoElasticSearchConfig;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;


@RunWith(FeaturesRunner.class)
@LocalDeploy("org.nuxeo.elasticsearch.core:elasticsearch-config-test-contrib.xml")
@Features({RepositoryElasticSearchFeature.class})
public class ElasticSearchAdminTest {

    @Test
    public void verifyNodeStartedWithConfig() throws Exception {

        ElasticSearchService ess = Framework.getLocalService(ElasticSearchService.class);
        Assert.assertNotNull(ess);

        ElasticSearchAdmin esa = Framework.getLocalService(ElasticSearchAdmin.class);
        Assert.assertNotNull(esa);

        NuxeoElasticSearchConfig config = esa.getConfig();
        Assert.assertEquals("nuxeoTestNode", config.getNodeName());
        Assert.assertEquals("nuxeoTestCluster", config.getClusterName());

        NodesInfoResponse nodeInfoResponse = ess.getClient().admin().cluster().nodesInfo(new NodesInfoRequest()).actionGet();

        Assert.assertEquals("nuxeoTestCluster", nodeInfoResponse.getClusterNameAsString());
        Assert.assertEquals(1, nodeInfoResponse.getNodes().length);
        Assert.assertEquals("nuxeoTestNode",nodeInfoResponse.getNodes()[0].getNode().getName());

   }
}
