package com.test.demo.service.serviceimpl;

import com.test.demo.dto.CreateGenreDTO;
import com.test.demo.entity.Genre;
import com.test.demo.reponsitory.GenreRepository;
import com.test.demo.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public Genre createGenre(CreateGenreDTO dto) {
        if (dto == null || dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên thể loại không được để trống");
        }

        // Chuẩn hóa tên: Loại bỏ tiền tố "Phim", chuẩn hóa ký tự tiếng Việt và không
        // phân biệt hoa thường
        String normalizedName = normalizeName(dto.getName());

        // Kiểm tra trùng lặp
        Optional<Genre> existingGenre = genreRepository.findByNormalizedName(normalizedName);
        if (existingGenre.isPresent()) {
            System.out.println("Thể loại đã tồn tại: " + normalizedName);
            return existingGenre.get();
        }

        Genre genre = new Genre();
        genre.setName(dto.getName()); // Lưu tên gốc
        genre.setNormalizedName(normalizedName); // Lưu tên chuẩn hóa
        System.out.println("Lưu thể loại: " + dto.getName());
        return genreRepository.save(genre);
    }

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        return genres != null ? genres : Collections.emptyList();
    }

    @Override
    public Optional<Genre> getGenreById(Long id) {
        return genreRepository.findById(id);
    }

    @Override
    public Genre updateGenre(Long id, CreateGenreDTO dto) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found with id: " + id));
        genre.setName(dto.getName());
        genre.setNormalizedName(normalizeName(dto.getName()));
        return genreRepository.save(genre);
    }

    @Override
    public void deleteGenre(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new RuntimeException("Genre not found with id: " + id);
        }
        genreRepository.deleteById(id);
    }

    // Hàm chuẩn hóa tên
    private String normalizeName(String name) {
        // Loại bỏ tiền tố "Phim" và khoảng trắng thừa
        String normalized = name.replaceFirst("(?i)^Phim\\s*", "").trim();

        // Chuyển đổi ký tự tiếng Việt thành không dấu và chuẩn hóa
        String temp = Normalizer.normalize(normalized, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        normalized = pattern.matcher(temp).replaceAll("").toLowerCase();

        return normalized;
    }
}