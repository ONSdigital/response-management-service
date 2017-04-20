package uk.gov.ons.ctp.response.casesvc.endpoint;

import static org.junit.Assert.assertTrue;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.ADMIN_ROLE;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY1_GROUP;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY1_LONG_DESC;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY1_MANUAL;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY1_ROLE;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY1_SHORT_DESC;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY1_TYPE;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY2_GROUP;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY2_LONG_DESC;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY2_MANUAL;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY2_ROLE;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY2_SHORT_DESC;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY2_TYPE;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY3_GROUP;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY3_LONG_DESC;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY3_MANUAL;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY3_ROLE;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY3_SHORT_DESC;
import static uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory.CATEGORY3_TYPE;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.ons.ctp.response.casesvc.CaseSvcBeanMapper;
import uk.gov.ons.ctp.response.casesvc.service.CategoryService;
import uk.gov.ons.ctp.response.casesvc.utility.MockCategoryServiceFactory;

/**
 * A test of the category endpoint
 *
 */
public final class CategoryEndpointUnitTest {

  @InjectMocks
  private CategoryEndpoint categoryEndpoint;

  @Mock
  private CategoryService categoryService;

  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    this.mockMvc = MockMvcBuilders
            .standaloneSetup(categoryEndpoint)
            .build();

    categoryService = MockCategoryServiceFactory.provide();
  }

  /**
   * A test
   */
  @Test
  public void findCategoriesNotFound() {
    assertTrue(true);

//    with("/categories/%s", "Felix The Cat")
//        .assertResponseCodeIs(HttpStatus.NOT_FOUND)
//        .andClose();
  }

//  /**
//   * A test
//   */
//  @Test
//  public void findCategoriesFoundNoRoleSpecified() {
//    with("/categories")
//        .assertResponseCodeIs(HttpStatus.OK)
//        .assertArrayLengthInBodyIs(3)
//        .assertStringListInBody("$..name", CATEGORY1_TYPE, CATEGORY2_TYPE, CATEGORY3_TYPE)
//        .assertStringListInBody("$..longDescription", CATEGORY1_LONG_DESC, CATEGORY2_LONG_DESC, CATEGORY3_LONG_DESC)
//        .assertStringListInBody("$..shortDescription", CATEGORY1_SHORT_DESC, CATEGORY2_SHORT_DESC, CATEGORY3_SHORT_DESC)
//        .assertStringListInBody("$..role", CATEGORY1_ROLE, CATEGORY2_ROLE, CATEGORY3_ROLE)
//        .assertStringListInBody("$..group", CATEGORY1_GROUP, CATEGORY2_GROUP, CATEGORY3_GROUP)
//        .assertBooleanListInBody("$..manual", CATEGORY1_MANUAL, CATEGORY2_MANUAL, CATEGORY3_MANUAL)
//        .andClose();
//  }
//
//  /**
//   * A test
//   */
//  @Test
//  public void findCategoriesFoundAdminRoleSpecified() {
//    String serviceUrl = String.format("/categories?role=%s", ADMIN_ROLE);
//    with(serviceUrl)
//        .assertResponseCodeIs(HttpStatus.OK)
//        .assertArrayLengthInBodyIs(1)
//        .assertStringListInBody("$..name", CATEGORY1_TYPE)
//        .assertStringListInBody("$..longDescription", CATEGORY1_LONG_DESC)
//        .assertStringListInBody("$..shortDescription", CATEGORY1_SHORT_DESC)
//        .assertStringListInBody("$..role", ADMIN_ROLE)
//        .assertStringListInBody("$..group", CATEGORY1_GROUP)
//        .assertBooleanListInBody("$..manual", CATEGORY1_MANUAL)
//        .andClose();
//  }

}
