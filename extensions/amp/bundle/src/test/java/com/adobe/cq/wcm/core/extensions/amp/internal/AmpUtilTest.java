/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2019 Adobe
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
package com.adobe.cq.wcm.core.extensions.amp.internal;

import java.util.HashSet;
import java.util.Set;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import com.adobe.cq.wcm.core.components.models.ExperienceFragment;
import com.adobe.cq.wcm.core.extensions.amp.AmpTestContext;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.Template;
import com.day.cq.wcm.foundation.AllowedComponentList;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


@ExtendWith(AemContextExtension.class)
class AmpUtilTest {

    @Mock
    private Resource resourceMock;

    @Mock
    private Page pageMock;

    @Mock
    private Template templateMock;

    @Mock
    private ResourceResolver resourceResolverMock;

    @Mock
    private ModelFactory modelFactory;

    @Mock
    private ExperienceFragment experienceFragment;

    private static final String TEST_BASE = "/amp-util";
    private static final String TEST_ROOT_PAGE = "/content";

    private String pathSample;
    private String[] searchPathSample;
    private String resourceTypeRegexSample;
    private Set<String> resourceTypesSample;

    private final AemContext context = AmpTestContext.newAemContext();

    @BeforeEach
    void setUp() {
        context.load().json(TEST_BASE + AmpTestContext.TEST_CONTENT_JSON, TEST_ROOT_PAGE);
        initMocks(this);
    }

    @Test
    void isAmpModeWithDefaults() {
        context.currentResource("/content/no-amp");
        assertEquals("", AmpUtil.getAmpMode(context.request()));
    }

    @Test
    void isAmpMode() {
        context.currentResource("/content/amp-only");
        assertEquals("ampOnly", AmpUtil.getAmpMode(context.request()));
    }


    @Test
    public void resolveResource_pathStartsWithSlash() {
        this.pathSample = "/fake/path/for/testing";

        when(this.resourceResolverMock.getResource(this.pathSample))
                .thenReturn(this.resourceMock);

        assertEquals(this.resourceMock, AmpUtil.resolveResource(this.resourceResolverMock, this.pathSample));
    }

    @Test
    public void resolveResource_pathStartsWithoutSlashResourceNull() {
        this.pathSample = "fake/path/for/testing";
        this.searchPathSample = new String[] { "/base/fake/path" };

        when(this.resourceResolverMock.getSearchPath())
                .thenReturn(this.searchPathSample);
        when(this.resourceResolverMock.getResource(this.searchPathSample[0] + this.pathSample))
                .thenReturn(null);

        assertEquals(null, AmpUtil.resolveResource(this.resourceResolverMock, this.pathSample));
    }

    @Test
    public void resolveResource_pathStartsWithoutSlashResourceNotNull() {
        this.pathSample = "fake/path/for/testing";
        this.searchPathSample = new String[] { "/base/fake/path" };

        when(this.resourceResolverMock.getSearchPath())
                .thenReturn(this.searchPathSample);
        when(this.resourceResolverMock.getResource(this.searchPathSample[0] + this.pathSample))
                .thenReturn(this.resourceMock);

        assertEquals(this.resourceMock, AmpUtil.resolveResource(this.resourceResolverMock, this.pathSample));
    }

}
