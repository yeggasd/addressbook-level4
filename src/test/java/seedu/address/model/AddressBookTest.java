package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_UNUSED;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.SecurityUtil;
import seedu.address.model.alias.Alias;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;

public class AddressBookTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final AddressBook addressBook = new AddressBook();

    private final AddressBook addressBookWithAmyAndBob = new AddressBookBuilder().withPerson(AMY)
            .withPerson(BOB).build();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
        assertEquals(Collections.emptyList(), addressBook.getTagList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        addressBook.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsAssertionError() {
        // Repeat ALICE twice
        List<Person> newPersons = Arrays.asList(ALICE, ALICE);
        List<Tag> newTags = new ArrayList<>(ALICE.getTags());
        List<Alias> newAliases = new ArrayList<>();
        AddressBookStub newData = new AddressBookStub(newPersons, newTags, newAliases);

        thrown.expect(AssertionError.class);
        addressBook.resetData(newData);
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getPersonList().remove(0);
    }

    @Test
    public void getTagList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getTagList().remove(0);
    }

    //@@author Caijun7-reused
    @Test
    public void updatePerson_detailsChanged_personsAndTagsListUpdated() throws Exception {
        AddressBook addressBookUpdatedToAmy = new AddressBookBuilder().withPerson(BOB).build();
        addressBookUpdatedToAmy.updatePerson(BOB, AMY);
        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(AMY).build();
        assertEquals(expectedAddressBook, addressBookUpdatedToAmy);
    }

    @Test
    public void removeTag_tagNotInUsed_addressBookUnchanged() throws Exception {
        addressBookWithAmyAndBob.removeTag(new Tag(VALID_TAG_UNUSED));
        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(AMY).withPerson(BOB).build();

        assertEquals(expectedAddressBook, addressBookWithAmyAndBob);
    }

    @Test
    public void removeTag_tagUsedByMultiplePersons_tagRemoved() throws Exception {
        addressBookWithAmyAndBob.removeTag(new Tag(VALID_TAG_FRIEND));
        Person amyWithoutFriendTag = new PersonBuilder(AMY).withTags().build();
        Person bobWithoutFriendTag = new PersonBuilder(BOB).withTags(VALID_TAG_HUSBAND).build();
        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(amyWithoutFriendTag)
                .withPerson(bobWithoutFriendTag).build();

        assertEquals(expectedAddressBook, addressBookWithAmyAndBob);
    }
    //@@author

    //@@author yeggasd
    @Test
    public void createdWithPassword_passwordChanged_passwordChanged() throws Exception {
        AddressBook addressBookUpdatedPassword = new AddressBook("new");
        Password expectedPassword = new Password("new");
        assertEquals(expectedPassword, addressBookUpdatedPassword.getPassword());
    }

    @Test
    public void updatePasswordWithClass_passwordChanged_passwordUpdated() throws Exception {
        AddressBook addressBookUpdatedPassword = new AddressBookBuilder().withPerson(BOB).withPassword("test").build();
        addressBookUpdatedPassword.updatePassword(new Password("new"));
        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(BOB).withPassword("new").build();
        assertEquals(expectedAddressBook, addressBookUpdatedPassword);
    }

    @Test
    public void updatePasswordWithBytes_passwordChanged_passwordUpdated() throws Exception {
        AddressBook addressBookUpdatedPassword = new AddressBookBuilder().withPerson(BOB).withPassword("test").build();
        addressBookUpdatedPassword.updatePassword(SecurityUtil.hashPassword("new"));
        addressBookUpdatedPassword.updatePassword(SecurityUtil.hashPassword("new"));
        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(BOB).withPassword("new").build();
        assertEquals(expectedAddressBook, addressBookUpdatedPassword);
    }
    //@@author

    /**
     * A stub ReadOnlyAddressBook whose persons and tags lists can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Tag> tags = FXCollections.observableArrayList();
        //@@author jingyinno
        private final ObservableList<Alias> aliases = FXCollections.observableArrayList();
        //@@author
        private final Password password = new Password("test");

        AddressBookStub(Collection<Person> persons, Collection<? extends Tag> tags, Collection<Alias> aliases) {
            this.persons.setAll(persons);
            this.tags.setAll(tags);
            this.aliases.setAll(aliases);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Tag> getTagList() {
            return tags;
        }

        //@@author jingyinno
        @Override
        public ObservableList<Alias> getAliasList() {
            return aliases;
        }

        @Override
        public HashMap<String, String> getAliasMapping() {
            return new HashMap<>();
        }

        @Override
        public ArrayList<ArrayList<String>> getUiFormattedAliasList() {
            return null;
        }

        @Override
        public void resetAliasList() {
            fail("This method should not be called.");
        }
        //@@author

        //@@author yeggasd
        @Override
        public Password getPassword() {
            return password;
        }
        //@@author
    }

}
