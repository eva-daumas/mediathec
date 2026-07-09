package com.mediathec.bookService;

import com.mediathec.bookService.entity.Book;
import com.mediathec.bookService.repository.BookRepository;
import com.mediathec.bookService.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    // ============================================================
    // MOCKS & INJECTION
    // ============================================================

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    // ============================================================
    // MES 5 LIVRES
    // ============================================================

    private Book harryPotterBook;        // ID 1
    private Book gameOfThronesBook;      // ID 2
    private Book strangerBook;           // ID 4
    private Book prideAndPrejudiceBook;  // ID 7
    private Book lesMiserablesBook;      // ID 8
    private List<Book> allBooks;
    private Book updatedBookDetails;

    @BeforeEach
    void setUp() {
        // 1. Harry Potter (ID 1)
        harryPotterBook = new Book();
        harryPotterBook.setId(1L);
        harryPotterBook.setTitle("Harry Potter à l'école des sorciers");
        harryPotterBook.setAuthor("J.K. Rowling");
        harryPotterBook.setCategory("Livre");
        harryPotterBook.setDescription("Le premier tome de la saga Harry Potter");
        harryPotterBook.setCoverImage("/assets/books/harry-potter.jpg");
        harryPotterBook.setAvailable(true);
        harryPotterBook.setCreatedAt(LocalDateTime.now());

        // 2. Le Trône de Fer (ID 2)
        gameOfThronesBook = new Book();
        gameOfThronesBook.setId(2L);
        gameOfThronesBook.setTitle("Le Trône de Fer");
        gameOfThronesBook.setAuthor("George R.R. Martin");
        gameOfThronesBook.setCategory("Livre");
        gameOfThronesBook.setDescription("Le premier tome de la saga Le Trône de Fer");
        gameOfThronesBook.setCoverImage("/assets/books/le-trone-de-fer.jpg");
        gameOfThronesBook.setAvailable(true);
        gameOfThronesBook.setCreatedAt(LocalDateTime.now());

        // 3. L'Étranger (ID 4)
        strangerBook = new Book();
        strangerBook.setId(4L);
        strangerBook.setTitle("L'Étranger");
        strangerBook.setAuthor("Albert Camus");
        strangerBook.setCategory("Livre");
        strangerBook.setDescription("Le célèbre roman d'Albert Camus");
        strangerBook.setCoverImage("/assets/books/letranger.jpg");
        strangerBook.setAvailable(true);
        strangerBook.setCreatedAt(LocalDateTime.now());

        // 4. Orgueil et Préjugés (ID 7)
        prideAndPrejudiceBook = new Book();
        prideAndPrejudiceBook.setId(7L);
        prideAndPrejudiceBook.setTitle("Orgueil et Préjugés");
        prideAndPrejudiceBook.setAuthor("Jane Austen");
        prideAndPrejudiceBook.setCategory("Livre");
        prideAndPrejudiceBook.setDescription("Le chef-d'œuvre de Jane Austen.");
        prideAndPrejudiceBook.setCoverImage("/assets/books/Orgueil-et-prejuges.jpg");
        prideAndPrejudiceBook.setAvailable(true);
        prideAndPrejudiceBook.setCreatedAt(LocalDateTime.now());

        // 5. Les Misérables (ID 8)
        lesMiserablesBook = new Book();
        lesMiserablesBook.setId(8L);
        lesMiserablesBook.setTitle("Les Misérables");
        lesMiserablesBook.setAuthor("Victor Hugo");
        lesMiserablesBook.setCategory("Livre");
        lesMiserablesBook.setDescription("Le chef-d'œuvre de Victor Hugo");
        lesMiserablesBook.setCoverImage("/assets/books/les-miserables.jpg");
        lesMiserablesBook.setAvailable(true);
        lesMiserablesBook.setCreatedAt(LocalDateTime.now());

        // Liste de tous vos livres (dans l'ordre de vos IDs)
        allBooks = Arrays.asList(
                harryPotterBook,        // ID 1
                gameOfThronesBook,      // ID 2
                strangerBook,           // ID 4
                prideAndPrejudiceBook,  // ID 7
                lesMiserablesBook       // ID 8
        );

        // Données pour la mise à jour
        updatedBookDetails = new Book();
        updatedBookDetails.setTitle("Harry Potter - Édition illustrée");
        updatedBookDetails.setAuthor("J.K. Rowling");
        updatedBookDetails.setCategory("Roman Fantastique");
        updatedBookDetails.setDescription("Nouvelle édition avec illustrations");
        updatedBookDetails.setCoverImage("/assets/books/harry-potter-illustre.jpg");
        updatedBookDetails.setAvailable(true);
    }

    // ============================================================
    // 1. TEST DE createBook()
    // ============================================================

    @Test
    void createBook_shouldSetAvailableTrueAndSave() {
        Book newBook = new Book();
        newBook.setTitle("Nouveau Livre");
        newBook.setAuthor("Nouvel Auteur");
        newBook.setCategory("Livre");
        newBook.setDescription("Description du nouveau livre");
        newBook.setCoverImage("/assets/books/nouveau.jpg");
        newBook.setAvailable(false);

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            savedBook.setId(9L); // Prochain ID disponible
            savedBook.setCreatedAt(LocalDateTime.now());
            return savedBook;
        });

        Book result = bookService.createBook(newBook);

        assertNotNull(result);
        assertEquals(9L, result.getId());
        assertEquals("Nouveau Livre", result.getTitle());
        assertTrue(result.isAvailable());
        assertNotNull(result.getCreatedAt());
        verify(bookRepository, times(1)).save(newBook);
    }

    @Test
    void createBook_shouldForceAvailableToTrue_EvenIfProvidedFalse() {
        Book bookWithFalseAvailable = new Book();
        bookWithFalseAvailable.setTitle("Test Book");
        bookWithFalseAvailable.setAuthor("Test Author");
        bookWithFalseAvailable.setAvailable(false);

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            savedBook.setId(10L);
            savedBook.setCreatedAt(LocalDateTime.now());
            return savedBook;
        });

        Book result = bookService.createBook(bookWithFalseAvailable);

        assertTrue(result.isAvailable());
        verify(bookRepository, times(1)).save(bookWithFalseAvailable);
    }

    @Test
    void createBook_shouldThrowException_WhenRepositoryFails() {
        Book bookToSave = new Book();
        bookToSave.setTitle("Book That Will Fail");

        when(bookRepository.save(any(Book.class)))
                .thenThrow(new RuntimeException("Erreur de base de données"));

        assertThrows(RuntimeException.class, () -> {
            bookService.createBook(bookToSave);
        });

        verify(bookRepository, times(1)).save(bookToSave);
    }

    // ============================================================
    // 2. TEST DE getBookById()
    // ============================================================

    @Test
    void getBookById_shouldReturnHarryPotter_WhenIdIs1() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(harryPotterBook));

        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Harry Potter à l'école des sorciers", result.getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBookById_shouldReturnGameOfThrones_WhenIdIs2() {
        when(bookRepository.findById(2L)).thenReturn(Optional.of(gameOfThronesBook));

        Book result = bookService.getBookById(2L);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Le Trône de Fer", result.getTitle());
        verify(bookRepository, times(1)).findById(2L);
    }

    @Test
    void getBookById_shouldReturnStranger_WhenIdIs4() {
        when(bookRepository.findById(4L)).thenReturn(Optional.of(strangerBook));

        Book result = bookService.getBookById(4L);

        assertNotNull(result);
        assertEquals(4L, result.getId());
        assertEquals("L'Étranger", result.getTitle());
        verify(bookRepository, times(1)).findById(4L);
    }

    @Test
    void getBookById_shouldReturnPrideAndPrejudice_WhenIdIs7() {
        when(bookRepository.findById(7L)).thenReturn(Optional.of(prideAndPrejudiceBook));

        Book result = bookService.getBookById(7L);

        assertNotNull(result);
        assertEquals(7L, result.getId());
        assertEquals("Orgueil et Préjugés", result.getTitle());
        verify(bookRepository, times(1)).findById(7L);
    }

    @Test
    void getBookById_shouldReturnLesMiserables_WhenIdIs8() {
        when(bookRepository.findById(8L)).thenReturn(Optional.of(lesMiserablesBook));

        Book result = bookService.getBookById(8L);

        assertNotNull(result);
        assertEquals(8L, result.getId());
        assertEquals("Les Misérables", result.getTitle());
        verify(bookRepository, times(1)).findById(8L);
    }

    @Test
    void getBookById_shouldReturnNull_WhenBookNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        Book result = bookService.getBookById(99L);

        assertNull(result);
        verify(bookRepository, times(1)).findById(99L);
    }

    @Test
    void getBookById_shouldReturnNull_WhenIdIsNull() {
        when(bookRepository.findById(null)).thenReturn(Optional.empty());

        Book result = bookService.getBookById(null);

        assertNull(result);
        verify(bookRepository, times(1)).findById(null);
    }

    // ============================================================
    // 3. TEST DE updateBook()
    // ============================================================

    @Test
    void updateBook_shouldUpdateHarryPotter_WhenIdIs1() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(harryPotterBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book result = bookService.updateBook(1L, updatedBookDetails);

        assertNotNull(result);
        assertEquals("Harry Potter - Édition illustrée", result.getTitle());
        assertEquals("Roman Fantastique", result.getCategory());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(harryPotterBook);
    }

    @Test
    void updateBook_shouldReturnNull_WhenBookNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        Book result = bookService.updateBook(99L, updatedBookDetails);

        assertNull(result);
        verify(bookRepository, times(1)).findById(99L);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBook_shouldReturnNull_WhenIdIsNull() {
        when(bookRepository.findById(null)).thenReturn(Optional.empty());

        Book result = bookService.updateBook(null, updatedBookDetails);

        assertNull(result);
        verify(bookRepository, times(1)).findById(null);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBook_shouldOnlyUpdateProvidedFields() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(harryPotterBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book partialUpdate = new Book();
        partialUpdate.setTitle("Harry Potter - Nouveau Titre");

        Book result = bookService.updateBook(1L, partialUpdate);

        assertNotNull(result);
        assertEquals("Harry Potter - Nouveau Titre", result.getTitle());
        assertNull(result.getCategory());
        assertNull(result.getDescription());
        assertNull(result.getCoverImage());

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(harryPotterBook);
    }

    // ============================================================
    // 4. TEST DE getAllBooks()
    // ============================================================

    @Test
    void getAllBooks_shouldReturnAll5Books() {
        when(bookRepository.findAll()).thenReturn(allBooks);

        List<Book> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        assertEquals(4L, result.get(2).getId());
        assertEquals(7L, result.get(3).getId());
        assertEquals(8L, result.get(4).getId());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getAllBooks_shouldReturnEmptyList_WhenNoBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList());

        List<Book> result = bookService.getAllBooks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookRepository, times(1)).findAll();
    }

    // ============================================================
    // 5. TEST DE deleteBook()
    // ============================================================

    @Test
    void deleteBook_shouldDeleteHarryPotter_WhenIdIs1() {
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBook_shouldNotThrowException_WhenIdDoesNotExist() {
        doNothing().when(bookRepository).deleteById(99L);
        assertDoesNotThrow(() -> bookService.deleteBook(99L));
        verify(bookRepository, times(1)).deleteById(99L);
    }

    // ============================================================
    // 6. TEST DE searchBooks()
    // ============================================================

    @Test
    void searchBooks_shouldReturnHarryPotter_WhenSearchingHarry() {
        when(bookRepository.findByTitleContainingIgnoreCase("Harry"))
                .thenReturn(Arrays.asList(harryPotterBook));

        List<Book> result = bookService.searchBooks("Harry");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Harry Potter à l'école des sorciers", result.get(0).getTitle());
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase("Harry");
    }

    @Test
    void searchBooks_shouldReturnEmptyList_WhenNoMatch() {
        when(bookRepository.findByTitleContainingIgnoreCase("Inexistant"))
                .thenReturn(Arrays.asList());

        List<Book> result = bookService.searchBooks("Inexistant");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase("Inexistant");
    }

    // ============================================================
    // 7. TEST DE getAvailableBooks()
    // ============================================================

    @Test
    void getAvailableBooks_shouldReturnAll5Books_WhenAllAvailable() {
        when(bookRepository.findByAvailableTrue()).thenReturn(allBooks);

        List<Book> result = bookService.getAvailableBooks();

        assertNotNull(result);
        assertEquals(5, result.size());
        verify(bookRepository, times(1)).findByAvailableTrue();
    }

    @Test
    void getAvailableBooks_shouldReturnEmptyList_WhenNoBooksAvailable() {
        when(bookRepository.findByAvailableTrue()).thenReturn(Arrays.asList());

        List<Book> result = bookService.getAvailableBooks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookRepository, times(1)).findByAvailableTrue();
    }

    // ============================================================
    // 8. TEST DE updateAvailability()
    // ============================================================

    @Test
    void updateAvailability_shouldSetHarryPotterToUnavailable() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(harryPotterBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book result = bookService.updateAvailability(1L, false);

        assertNotNull(result);
        assertFalse(result.isAvailable());
        verify(bookRepository, times(1)).save(harryPotterBook);
    }

    @Test
    void updateAvailability_shouldSetLesMiserablesToAvailable() {
        lesMiserablesBook.setAvailable(false);
        when(bookRepository.findById(8L)).thenReturn(Optional.of(lesMiserablesBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book result = bookService.updateAvailability(8L, true);

        assertNotNull(result);
        assertTrue(result.isAvailable());
        verify(bookRepository, times(1)).save(lesMiserablesBook);
    }

    @Test
    void updateAvailability_shouldReturnNull_WhenBookNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        Book result = bookService.updateAvailability(99L, false);

        assertNull(result);
        verify(bookRepository, never()).save(any());
    }

    // ============================================================
    // 9. TEST DE getBooksByCategory()
    // ============================================================

    @Test
    void getBooksByCategory_shouldReturnAll5Books_WhenCategoryLivre() {
        when(bookRepository.findByCategory("Livre")).thenReturn(allBooks);

        List<Book> result = bookService.getBooksByCategory("Livre");

        assertNotNull(result);
        assertEquals(5, result.size());
        verify(bookRepository, times(1)).findByCategory("Livre");
    }

    @Test
    void getBooksByCategory_shouldReturnEmptyList_WhenCategoryNotFound() {
        when(bookRepository.findByCategory("BD")).thenReturn(Arrays.asList());

        List<Book> result = bookService.getBooksByCategory("BD");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookRepository, times(1)).findByCategory("BD");
    }

    // ============================================================
    // 10. TEST DE findByTitle()
    // ============================================================

    @Test
    void findByTitle_shouldReturnHarryPotter_WhenTitleExists() {
        when(bookRepository.findByTitleContainingIgnoreCase("Harry Potter à l'école des sorciers"))
                .thenReturn(Arrays.asList(harryPotterBook));

        Book result = bookService.findByTitle("Harry Potter à l'école des sorciers");

        assertNotNull(result);
        assertEquals("Harry Potter à l'école des sorciers", result.getTitle());
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase("Harry Potter à l'école des sorciers");
    }

    @Test
    void findByTitle_shouldReturnNull_WhenTitleNotFound() {
        when(bookRepository.findByTitleContainingIgnoreCase("Inexistant"))
                .thenReturn(Arrays.asList());

        Book result = bookService.findByTitle("Inexistant");

        assertNull(result);
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase("Inexistant");
    }
}