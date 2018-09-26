package testutils.gettersetters;

import static com.sun.xml.internal.ws.util.StringUtils.capitalize;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class StringGetterSetterTester {
    private static final String VALUE = "blah";

    private StringGetterSetterTester() {
        //private default constructor
    }

    /**
     * Tests all of the Getter and Setter methods in a class for String variables.
     * Does not test nullable/nonNullable status.
     *
     * @param objectUnderTest object to test getters and setters of
     */
    public static void assertStringGettersAndSetters(Object objectUnderTest) {
        assertStringGettersAndSetters(objectUnderTest, false, true, new String[0]);
    }

    /**
     * Tests all of the Getter and Setter methods in a class for String variables.
     * Does not test nullable/nonNullable status.
     *
     * @param objectUnderTest object to test getters and setters of
     * @param fieldsToIgnore  array of field names to ignore
     */
    public static void assertStringGettersAndSetters(Object objectUnderTest, String[] fieldsToIgnore) {
        assertStringGettersAndSetters(objectUnderTest, false, true, fieldsToIgnore);
    }


    /**
     * Tests all of the Getter and Setter methods in a class for String variables.
     * Also validates that values can be set to null.
     *
     * @param objectUnderTest object to test getters and setters of
     */
    public static void assertNullableStringGettersAndSetters(Object objectUnderTest) {
        assertStringGettersAndSetters(objectUnderTest, true, true, new String[0]);
    }

    /**
     * Tests all of the Getter and Setter methods in a class for String variables.
     * Also validates that values can be set to null.
     *
     * @param objectUnderTest object to test getters and setters of
     * @param fieldsToIgnore  array of field names to ignore
     */
    public static void assertNullableStringGettersAndSetters(Object objectUnderTest, String[] fieldsToIgnore) {
        assertStringGettersAndSetters(objectUnderTest, true, true, fieldsToIgnore);
    }

    /**
     * Tests all of the Getter and Setter methods in a class for String variables.
     * Also validates that null values cause an NPE
     *
     * @param objectUnderTest object to test getters and setters of
     */
    public static void assertNonNullStringGettersAndSetters(Object objectUnderTest) {
        assertStringGettersAndSetters(objectUnderTest, true, false, new String[0]);
    }

    /**
     * Tests all of the Getter and Setter methods in a class for String variables.
     * Also validates that null values cause an NPE
     *
     * @param objectUnderTest object to test getters and setters of
     * @param fieldsToIgnore  array of field names to ignore
     */
    public static void assertNonNullStringGettersAndSetters(Object objectUnderTest, String[] fieldsToIgnore) {
        assertStringGettersAndSetters(objectUnderTest, true, false, fieldsToIgnore);
    }

    private static void assertStringGettersAndSetters(Object objectUnderTest, boolean testNulls, boolean nullable, String[] fieldsToIgnore) {
        Field[] fields = objectUnderTest.getClass().getDeclaredFields();
        List<String> ignoreList = Arrays.asList(fieldsToIgnore);

        for (Field field : fields) {
            Class type = field.getType();
            if (type == String.class) {
                String fieldName = field.getName();

                if (ignoreList.contains(fieldName)) {
                    continue;
                }

                try {
                    assertGetterSetter(objectUnderTest, fieldName, testNulls, nullable);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
                    continue;
                }
            }
        }
    }

    private static void assertGetterSetter(Object objectUnderTest, String fieldName, boolean testNulls, boolean nullable) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class classUnderTest = objectUnderTest.getClass();

        String capitalizedField = capitalize(fieldName);

        Method getter = classUnderTest.getMethod("get" + capitalizedField, new Class[]{});
        Method setter = classUnderTest.getMethod("set" + capitalizedField, new Class[]{String.class});

        setter.invoke(objectUnderTest, VALUE);
        assertEquals(fieldName + " - failed to set and get.", VALUE, getter.invoke(objectUnderTest));

        if (testNulls) {
            if (!nullable) {
                setter.invoke(objectUnderTest, (String) null);
                fail("NPE not thrown for set" + capitalizedField);

            } else {
                setter.invoke(objectUnderTest, (String) null);
                assertNull(fieldName + " couldn't be set and get as null", getter.invoke(objectUnderTest));
            }
        }
    }
}
