package com.geupjo.koreantiger.service;

import com.geupjo.koreantiger.common.exception.CustomException;
import com.geupjo.koreantiger.common.exception.ErrorCode;
import com.geupjo.koreantiger.dto.StudentRankingDto;
import com.geupjo.koreantiger.dto.response.RankingBoardResponseDto;
import com.geupjo.koreantiger.dto.response.StudentHistoryResponseDto;
import com.geupjo.koreantiger.dto.response.StudentProfileResponseDto;
import com.geupjo.koreantiger.entity.Class;
import com.geupjo.koreantiger.entity.*;
import com.geupjo.koreantiger.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StudentLmsService {
    private final MemberRepository memberRepository;
    private final EducationProfileRepository educationProfileRepository;
    private final EducationHistoryRepository educationHistoryRepository;
    private final LectureRepository lectureRepository;
    private final ClassRepository classRepository;

    private static final int ONE_YEAR = 1;

    public StudentProfileResponseDto getStudentProfile(Long studentId) {
        Member student = memberRepository.findById(studentId).orElseThrow(() -> new CustomException(ErrorCode.NO_MATCH_USER_EXCEPTION));
        EducationProfile profile = educationProfileRepository.findByMemberId(student.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_MATCH_USER_EXCEPTION));
        EducationHistory lastHistory = educationHistoryRepository.findFirstByMemberIdAndAttendanceIsFalseOrderByCreatedAt(student.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_MATCH_USER_EXCEPTION));
        long currentTime = System.currentTimeMillis();
        long lastConnection = lastHistory.getLearningStop();
        long duration = currentTime - lastConnection / (1000 * 60 * 60 * 24);
        int connection = (int) duration;
        System.out.println(duration);

        return new StudentProfileResponseDto(
                student.getName(),
                profile.getExperience(),
                connection,
                profile.getStudentProfileTitle());
    }

    public RankingBoardResponseDto getRankingBoard(Long studentId) {
        //학교랭킹 50위 레벨 및 경험치순으로 정렬
        Class studentClass = classRepository.findByStudentId(studentId).orElseThrow(() -> new CustomException(ErrorCode.NO_MATCH_USER_EXCEPTION));
        List<Class> classes = classRepository.findAllByClassId(studentClass.getClassId());
        List<Long> ClassmemberIds = classes
                .stream()
                .filter(Objects::nonNull)
                .map(Class::getStudentId)
                .toList();
        List<EducationProfile> inSchoolProfiles = educationProfileRepository.findTop50ByMemberIdInOrderByLevelDescExperienceDesc(ClassmemberIds);
        ArrayList<StudentRankingDto> inSchoolRankingBoard = new ArrayList<>();
        getRanking50(inSchoolProfiles, inSchoolRankingBoard);

        //전체랭킹 50위 레벨 및 경험치순으로 정렬
        List<EducationProfile> educationProfiles = educationProfileRepository.findTop50ByOrderByLevelDescExperienceDesc();
        ArrayList<StudentRankingDto> totalRankingBoard = new ArrayList<>();
        getRanking50(educationProfiles, totalRankingBoard);
        return new RankingBoardResponseDto(inSchoolRankingBoard, totalRankingBoard);
    }

    private void getRanking50(List<EducationProfile> educationProfiles, ArrayList<StudentRankingDto> RankingBoard) {
        List<Long> memberIds = educationProfiles
                .stream()
                .filter(Objects::nonNull)
                .map(EducationProfile::getMemberId)
                .toList();
        List<Member> RankingMembers = memberRepository.findByIdIn(memberIds);

        int totalRank = 1;
        int index = 0;
        for (EducationProfile eachProfile : educationProfiles) {
            StudentRankingDto dto = new StudentRankingDto(
                    totalRank,
                    RankingMembers.get(index).getName(),
                    eachProfile.getLevel(),
                    eachProfile.getProgress()
            );
            RankingBoard.add(dto);
            totalRank++;
        }
    }

    public StudentHistoryResponseDto getStudentEducationHistories(Member currentStudent) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime oneYearBeforeToday = today.minusYears(ONE_YEAR);

        List<EducationHistory> histories = educationHistoryRepository
                .findByMemberIdAndCreatedAtBetween(currentStudent.getId(), oneYearBeforeToday, today);
        List<Lecture> lectures = lectureRepository
                .findByMemberIdAndCreatedAtBetween(currentStudent.getId(), oneYearBeforeToday, today);

        return StudentHistoryResponseDto.of(histories, lectures);
    }
}
