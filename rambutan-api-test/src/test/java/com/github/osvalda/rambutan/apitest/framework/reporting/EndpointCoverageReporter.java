package com.github.osvalda.rambutan.apitest.framework.reporting;

import com.github.osvalda.rambutan.apitest.framework.supplementary.TestCaseSupplementary;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

@Slf4j
public class EndpointCoverageReporter implements IReporter {

    private int allEndpointsNum = 0;
    private Map<String, Integer> coverageMap = new HashMap<>();
    private Multimap<String, String> endpointByAreas = ArrayListMultimap.create();
    private List<CoverageObject> coverages = new ArrayList<>();
    private Map<String, AreaWiseCoverageObject> areaWiseCoverages = new TreeMap<>();

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        String dateAndTime = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));

        Configuration cfg = getTemplateConfiguration();
        Map<String, Object> templateInput = new HashMap<>();

        createInitialCoverageMaps();
        createTheEndpointTableInput(suites);
        collectEndpointsDetails();


        templateInput.put("coveredEndpointsNum", countCoveredEndpoints());
        templateInput.put("endpoints", coverages);
        templateInput.put("allEndpointsNum", allEndpointsNum);
        templateInput.put("areaWiseEndpoints", areaWiseCoverages);
        templateInput.put("currentDateAndTime", dateAndTime);

        try {
            Template template = cfg.getTemplate("coverageReportTemplate.ftl");
            Writer fileWriter = new FileWriter(new File("coverageReport.html"));
            template.process(templateInput, fileWriter);
            log.info("The coverage report has been created.");
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void createTheEndpointTableInput(List<ISuite> suites) {
        suites.forEach(e -> e.getResults().entrySet().forEach(this::processTheResults));
    }

    private void processTheResults(Map.Entry<String, ISuiteResult> stringISuiteResultEntry) {
            ITestContext testContext = stringISuiteResultEntry.getValue().getTestContext();

            Set<ITestResult> failedTests
                    = testContext.getFailedTests().getAllResults();
            Set<ITestResult> passedTests
                    = testContext.getPassedTests().getAllResults();
            Set<ITestResult> skippedTests
                    = testContext.getSkippedTests().getAllResults();

            Stream.of(failedTests, passedTests, skippedTests).forEach(e -> e.forEach(this::updateCoverage));
    }

    private void updateCoverage(ITestResult testResult) {
        if (testResult.getMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(TestCaseSupplementary.class)) {
            Arrays.asList(testResult.getMethod().getConstructorOrMethod().getMethod()
                    .getAnnotation(TestCaseSupplementary.class).api()).forEach(api -> {
                if(coverageMap.containsKey(api)) {
                    int actCoverageLevel = coverageMap.get(api);
                    coverageMap.put(api, actCoverageLevel+1);
                }
            });
        }
    }

    private int countCoveredEndpoints() {
        return (int) coverageMap.values().stream().filter(e -> e > 0).count();
    }

    private void collectEndpointsDetails() {
        endpointByAreas.keySet().forEach(area -> endpointByAreas.get(area)
                .forEach(endpoint -> coverages.add(new CoverageObject(area, endpoint))));
        coverages.forEach(endpoint -> {
            endpoint.setCovered(coverageMap.get(endpoint.getMethod() + " " + endpoint.getEndpoint()));
            if (areaWiseCoverages.containsKey(endpoint.getArea())) {
                areaWiseCoverages.get(endpoint.getArea()).increaseCoverage(endpoint.getCovered());
            } else {
                areaWiseCoverages.put(endpoint.getArea(), new AreaWiseCoverageObject(endpoint.getCovered()));
            }
        });
    }

    private Configuration getTemplateConfiguration() {
        Configuration cfg = new Configuration(new Version(2, 3, 20));
        cfg.setClassForTemplateLoading(EndpointCoverageReporter.class, "/reportTemplates/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return cfg;
    }

    private void createInitialCoverageMaps() {
        try {
            File file = FileUtils.getFile((getClass().getClassLoader()
                    .getResource("endpoints/all_endpoints.txt"))
                    .getPath());

            FileUtils.readLines(file, "UTF-8").forEach(e -> {
                if(!e.isEmpty()) {
                    String[] line = StringUtils.splitByWholeSeparator(e, ", ");
                    if(line.length == 2) {
                        endpointByAreas.put(line[1], line[0]);
                        coverageMap.put(line[0], 0);
                    }
                }
            });
            allEndpointsNum = coverageMap.keySet().size();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
