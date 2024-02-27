package online.caltuli.consumer.api.abuseipdb;

import online.caltuli.consumer.api.abuseipdb.exception.ConfigurationLoadException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.client.ResponseHandler;
import static org.mockito.ArgumentMatchers.any;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.function.Supplier;

@Tag("AbuseIpDbClient")
@DisplayName("AbuseIpDbClient class tests")
public class AbuseIpDbClientTest {

    @Nested
    @Tag("loadProperties")
    @DisplayName("loadProperties method tests")
    public class loadProperties {

        private AbuseIpDbClient abuseIpDbClient;
        private Field propertiesField;

        @BeforeEach
        public void setUp() throws Exception {
            // obtain an instance of AbuseIpDbClient, typically a singleton
            abuseIpDbClient = AbuseIpDbClient.getInstance();
            // access and clear the 'properties' field of the AbuseIpDbClient class to simulate a state before properties are loaded
            propertiesField = AbuseIpDbClient.class.getDeclaredField("properties");
            propertiesField.setAccessible(true); // make attribute 'properties' accessible although it's private
            Properties newProperties = new Properties();
            propertiesField.set(null, newProperties); // erase properties
        }

        @Test
        // demonstrates testing a method with restricted access or dependencies using reflection
        @DisplayName("loadProperties should load properties successfully when properties file exists")
        public void loadProperties_ShouldLoadPropertiesSuccessfully_WhenPropertiesFileExists() throws Exception {

            // Arrange

            // prepare the 'inputStreamSupplier' field to return an InputStream with predefined content, simulating the presence of a properties file
            Field supplierField = AbuseIpDbClient.class.getDeclaredField("inputStreamSupplier");
            supplierField.setAccessible(true);
            String propertiesContent = "key=value";
            Supplier<InputStream> mockSupplier = () -> new ByteArrayInputStream(propertiesContent.getBytes(StandardCharsets.UTF_8));
            supplierField.set(abuseIpDbClient, mockSupplier);

            // Act and Assert

            // reflectively access and invoke the 'loadProperties' method with the mockSupplier to test property loading functionality
            assertThatCode(() -> {
                Method loadPropertiesMethod = AbuseIpDbClient.class.getDeclaredMethod("loadProperties", Supplier.class);
                loadPropertiesMethod.setAccessible(true);
                loadPropertiesMethod.invoke(AbuseIpDbClient.class, mockSupplier);
                assertThat(((Properties) propertiesField.get(null)).getProperty("key")).isEqualTo("value");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("loadProperties should throw ConfigurationLoadException when properties file does not exist")
        public void loadProperties_ShouldThrowConfigurationLoadException_WhenPropertiesFileDoesNotExist() throws Exception {

            // Arrange
            Supplier<InputStream> mockSupplier = () -> null; // no property file simulation
            Field propertiesFileField = AbuseIpDbClient.class.getDeclaredField("PROPERTIES_FILE");
            propertiesFileField.setAccessible(true);

            // Act & Assert
            assertThatThrownBy(() -> {
                Method loadPropertiesMethod = AbuseIpDbClient.class.getDeclaredMethod("loadProperties", Supplier.class);
                loadPropertiesMethod.setAccessible(true);
                try {
                    loadPropertiesMethod.invoke(AbuseIpDbClient.class, mockSupplier);
                } catch (InvocationTargetException ite) {
                    throw ite.getCause();
                }
            })
                    .isInstanceOf(ConfigurationLoadException.class)
                    .hasMessageContaining("Property file '" + propertiesFileField.get(null) + "' not found in the classpath");
        }

        @Test
        @DisplayName("loadProperties should throw ConfigurationLoadException when there is an error reading the properties file")
        public void loadProperties_ShouldThrowConfigurationLoadException_WhenErrorReadingPropertiesFile() throws Exception {

            // Arrange
            Supplier<InputStream> mockSupplier = () -> new InputStream() {
                @Override
                public int read() throws IOException {
                    throw new IOException("Error reading file");
                }
            };

            // Act & Assert
            assertThatThrownBy(() -> {
                Method loadPropertiesMethod = AbuseIpDbClient.class.getDeclaredMethod("loadProperties", Supplier.class);
                loadPropertiesMethod.setAccessible(true);
                try {
                    loadPropertiesMethod.invoke(AbuseIpDbClient.class, mockSupplier);
                } catch (InvocationTargetException ite) {
                    throw ite.getCause();
                }
            })
                    .isInstanceOf(ConfigurationLoadException.class)
                    .hasMessageContaining("Error loading properties file");
        }

        @Test
        @DisplayName("loadProperties should handle empty properties file without throwing exceptions")
        public void loadProperties_ShouldHandleEmptyPropertiesFile() throws Exception {
            // Arrange
            String emptyPropertiesContent = "";
            Supplier<InputStream> mockSupplier = () -> new ByteArrayInputStream(emptyPropertiesContent.getBytes(StandardCharsets.UTF_8));
            // Act & Assert
            assertThatCode(() -> {
                Method loadPropertiesMethod = AbuseIpDbClient.class.getDeclaredMethod("loadProperties", Supplier.class);
                loadPropertiesMethod.setAccessible(true);
                loadPropertiesMethod.invoke(abuseIpDbClient, mockSupplier);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("loadProperties should retain only the last value for duplicate keys")
        public void loadProperties_ShouldRetainLastValueForDuplicateKeys() throws Exception {
            // Arrange
            String duplicateKeysContent = "key1=value1\nkey1=value2";
            Supplier<InputStream> mockSupplier = () -> new ByteArrayInputStream(duplicateKeysContent.getBytes(StandardCharsets.UTF_8));
            // Act and Assert
            assertThatCode(() -> {
                Method loadPropertiesMethod = AbuseIpDbClient.class.getDeclaredMethod("loadProperties", Supplier.class);
                loadPropertiesMethod.setAccessible(true);
                loadPropertiesMethod.invoke(abuseIpDbClient, mockSupplier);

                Properties properties = (Properties) propertiesField.get(null);
                assertThat(properties.getProperty("key1")).isEqualTo("value2");
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @Tag("fetchIpDetailsAsJson")
    @Disabled
    @DisplayName("fetchIpDetailsAsJson method tests")
    public class fetchIpDetailsAsJson {

        private AbuseIpDbClient abuseIpDbClient ;
        private MockedStatic<Request> mockedStaticRequest;
        private Request mockRequest;
        Response mockResponse;
        Content mockContent;

        @BeforeEach
        public void setUp() throws ConfigurationLoadException {
            abuseIpDbClient = AbuseIpDbClient.getInstance();
            mockedStaticRequest = Mockito.mockStatic(Request.class);
            mockRequest = mock(Request.class);
            mockResponse = mock(Response.class);
            mockContent = mock(Content.class);
        }

        @AfterEach
        public void tearDown() {
            mockedStaticRequest.close();
        }

        @Test
        @DisplayName("fetchIpDetailsAsJson should return expected json when given well known IP")
        public void fetchIpDetailsAsJson_ShouldReturnExpectedJson_WhenGivenWellKnownIp() throws IOException {
            // Arrange
            String wellKnownIp = "8.8.8.8";
            String expectedJson = "{\"data\":{\"ipAddress\":\"8.8.8.8\",\"isPublic\":true,\"ipVersion\":4,\"isWhitelisted\":true,\"abuseConfidenceScore\":0,\"countryCode\":\"US\",\"usageType\":\"Data Center\\/Web Hosting\\/Transit\",\"isp\":\"Google LLC\",\"domain\":\"google.com\",\"hostnames\":[\"dns.google\"],\"isTor\":false,\"totalReports\":38,\"numDistinctUsers\":23,\"lastReportedAt\":\"2024-02-14T01:49:02+00:00\"}}";
            mockedStaticRequest.when(() -> Request.Get(Mockito.anyString())).thenReturn(mockRequest);
            when(mockRequest.addHeader(eq("Key"), Mockito.anyString())).thenReturn(mockRequest);
            when(mockRequest.addHeader(eq("Accept"), eq("application/json"))).thenReturn(mockRequest);
            when(mockRequest.execute()).thenReturn(mockResponse);
            when(mockResponse.returnContent()).thenReturn(mockContent);
            when(mockContent.asString()).thenReturn(expectedJson);
            // Act and Assert
            assertThatCode(() -> {
                final String response = abuseIpDbClient.fetchIpDetailsAsJson(wellKnownIp);
                assertThat(response).isEqualTo(expectedJson);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("fetchIpDetailsAsJson should throw IOException when HTTP request fails ")
        public void fetchIpDetailsAsJson_ShouldThrowIOException_WhenHttpRequestFails() throws IOException {
            // Arrange
            String ip = "8.8.8.8";
            mockedStaticRequest.when(() -> Request.Get(Mockito.anyString())).thenReturn(mockRequest);
            when(mockRequest.addHeader(eq("Key"), Mockito.anyString())).thenReturn(mockRequest);
            when(mockRequest.addHeader(eq("Accept"), eq("application/json"))).thenReturn(mockRequest);
            when(mockRequest.execute()).thenThrow(new IOException());
            // Act and Assert
            assertThatThrownBy( () -> abuseIpDbClient.fetchIpDetailsAsJson(ip))
                    .isInstanceOf(IOException.class);
        }
    }

    /*
    This section is intended to explore the use of reflection.

    @Nested
    @Tag("learn reflection")
    @DisplayName("learn reflection")
    public class ReflectionExample {

        @Test
        public void inspectClassMetaStructureOfAbuseIpDbClient() {

            //String[] args;

            // Obtain the Class object for AbuseIpDbClient
            Class<AbuseIpDbClient> clazz = AbuseIpDbClient.class;

            // Retrieve and print all declared fields
            System.out.println("Declared fields:");
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                System.out.println(field.getName());
            }

            // Retrieve and print all fields, including inherited ones
            System.out.println("\nPublic fields (including inherited):");
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                System.out.println(field.getName());
            }

            // Retrieve and print all declared methods
            System.out.println("\nDeclared methods:");
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                System.out.println(method.getName());
            }

            // Retrieve and print all methods, including inherited ones
            System.out.println("\nPublic methods (including inherited):");
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                System.out.println(method.getName());
            }
        }

        @Test
        public void exploreClassObjectRelatedToAbuseIpDbClient() {
            // Obtain the Class object for the Class associated with AbuseIpDbClient
            Class<?> clazz = AbuseIpDbClient.class.getClass();

            // Retrieve and print all declared fields of the Class object
            System.out.println("Declared fields of Class object:");
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                System.out.println(field.getName());
            }

            // Retrieve and print all public fields of the Class object, including inherited ones
            System.out.println("\nPublic fields of Class object (including inherited):");
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                System.out.println(field.getName());
            }

            // Retrieve and print all declared methods of the Class object
            System.out.println("\nDeclared methods of Class object:");
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                System.out.println(method.getName());
            }

            // Retrieve and print all public methods of the Class object, including inherited ones
            System.out.println("\nPublic methods of Class object (including inherited):");
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                System.out.println(method.getName());
            }
        }
    }

    The output of these two tests are :

    Declared fields:
    PROPERTIES_FILE
    properties
    instance
    baseUrl
    apiKey
    inputStreamSupplier
    $jacocoData

    Public fields (including inherited):

    Declared methods:
    getInstance
    lambda$new$0
    $jacocoInit
    loadProperties
    fetchIpDetailsAsJson

    Public methods (including inherited):
    getInstance
    equals
    toString
    hashCode
    getClass
    notify
    notifyAll
    wait
    wait
    wait
    Declared fields of Class object:
    ANNOTATION
    ENUM
    SYNTHETIC
    cachedConstructor
    name
    module
    packageName
    componentType
    allPermDomain
    reflectionData
    classRedefinedCount
    genericInfo
    EMPTY_CLASS_ARRAY
    serialVersionUID
    serialPersistentFields
    reflectionFactory
    enumConstants
    enumConstantDirectory
    annotationData
    annotationType
    classValueMap

    Public fields of Class object (including inherited):

    Declared methods of Class object:
    getName
    checkPackageAccess
    forName
    forName
    forName
    forName
    forName
    forName
    forName0
    toString
    getModule
    getProtectionDomain
    isAssignableFrom
    isInstance
    getModifiers
    isInterface
    isArray
    isPrimitive
    isHidden
    getSuperclass
    cast
    componentType
    componentType
    getClassLoader0
    describeConstable
    getComponentType
    isAnnotation
    isEnum
    isRecord
    getTypeParameters
    getClassLoader
    checkMemberAccess
    getReflectionFactory
    getConstructor0
    newInstance
    initClassName
    getGenericInfo
    elementType
    getInterfaces
    getInterfaces
    reflectionData
    getInterfaces0
    isMemberClass
    isLocalClass
    isAnonymousClass
    getClassAccessFlagsRaw
    getEnclosingMethodInfo
    getFactory
    toClass
    getEnclosingClass
    privateGetDeclaredMethods
    arrayContentsEq
    getEnclosingMethod0
    privateGetDeclaredConstructors
    getDeclaringClass0
    isUnnamedClass
    getSimpleName0
    getSimpleName
    getSimpleBinaryName
    getCanonicalName0
    getCanonicalName
    isLocalOrAnonymousClass
    isSynthetic
    isTopLevelClass
    getSimpleBinaryName0
    hasEnclosingMethodInfo
    privateGetPublicFields
    copyFields
    privateGetPublicMethods
    copyMethods
    copyConstructors
    getField0
    getMethod0
    methodToString
    getDeclaredClasses0
    privateGetDeclaredFields
    getRecordComponents0
    searchFields
    searchMethods
    resolveName
    isOpenToCaller
    getResourceAsStream
    getResource
    protectionDomain
    getProtectionDomain0
    getPackageName
    newReflectionData
    getGenericSignature0
    getExecutableTypeAnnotationBytes
    getDeclaredFields0
    addAll
    getDeclaredConstructors0
    getDeclaredMethods0
    getMethodsRecursive
    desiredAssertionStatus0
    desiredAssertionStatus
    isRecord0
    getEnumConstantsShared
    getMethod
    enumConstantDirectory
    cannotCastMsg
    annotationData
    isAnnotationPresent
    createAnnotationData
    getRawAnnotations
    getConstantPool
    casAnnotationType
    getRawTypeAnnotations
    getNestHost0
    getNestHost
    getNestMembers0
    descriptorString
    getPermittedSubclasses0
    checkPackageAccessForPermittedSubclasses
    getPermittedSubclasses
    getClassFileVersion0
    getClassAccessFlagsRaw0
    arrayType
    arrayType
    isDirectSubType
    registerNatives
    toGenericString
    typeVarBounds
    getClassData
    getGenericSuperclass
    getPackage
    getGenericInterfaces
    accessFlags
    getSigners
    setSigners
    getEnclosingMethod
    getEnclosingConstructor
    getDeclaringClass
    getTypeName
    getClasses
    getFields
    getMethods
    getConstructors
    getField
    getConstructor
    getDeclaredClasses
    getDeclaredFields
    getRecordComponents
    getDeclaredMethods
    getDeclaredConstructors
    getDeclaredField
    getDeclaredMethod
    getDeclaredPublicMethods
    getDeclaredConstructor
    getPrimitiveClass
    getEnumConstants
    asSubclass
    getAnnotation
    getAnnotationsByType
    getAnnotations
    getDeclaredAnnotation
    getDeclaredAnnotationsByType
    getDeclaredAnnotations
    getAnnotationType
    getDeclaredAnnotationMap
    getAnnotatedSuperclass
    getAnnotatedInterfaces
    isNestmateOf
    getNestMembers
    isSealed
    getClassFileVersion
    lambda$getPermittedSubclasses$2
    lambda$getPermittedSubclasses$1
    lambda$methodToString$0

    Public methods of Class object (including inherited):
    getName
    forName
    forName
    forName
    toString
    getModule
    getProtectionDomain
    isAssignableFrom
    isInstance
    getModifiers
    isInterface
    isArray
    isPrimitive
    isHidden
    getSuperclass
    cast
    componentType
    componentType
    describeConstable
    getComponentType
    isAnnotation
    isEnum
    isRecord
    getTypeParameters
    getClassLoader
    newInstance
    getInterfaces
    isMemberClass
    isLocalClass
    isAnonymousClass
    getEnclosingClass
    isUnnamedClass
    getSimpleName
    getCanonicalName
    isSynthetic
    getResourceAsStream
    getResource
    getPackageName
    desiredAssertionStatus
    getMethod
    isAnnotationPresent
    getNestHost
    descriptorString
    getPermittedSubclasses
    arrayType
    arrayType
    toGenericString
    getGenericSuperclass
    getPackage
    getGenericInterfaces
    accessFlags
    getSigners
    getEnclosingMethod
    getEnclosingConstructor
    getDeclaringClass
    getTypeName
    getClasses
    getFields
    getMethods
    getConstructors
    getField
    getConstructor
    getDeclaredClasses
    getDeclaredFields
    getRecordComponents
    getDeclaredMethods
    getDeclaredConstructors
    getDeclaredField
    getDeclaredMethod
    getDeclaredConstructor
    getEnumConstants
    asSubclass
    getAnnotation
    getAnnotationsByType
    getAnnotations
    getDeclaredAnnotation
    getDeclaredAnnotationsByType
    getDeclaredAnnotations
    getAnnotatedSuperclass
    getAnnotatedInterfaces
    isNestmateOf
    getNestMembers
    isSealed
    equals
    hashCode
    getClass
    notify
    notifyAll
    wait
    wait
    wait
    */
}
