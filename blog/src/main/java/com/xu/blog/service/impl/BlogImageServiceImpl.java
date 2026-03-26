package com.xu.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.blog.domain.BlogImage;
import com.xu.blog.mapper.BlogImageMapper;
import com.xu.blog.service.BlogImageService;
import org.springframework.stereotype.Service;

@Service
public class BlogImageServiceImpl extends ServiceImpl<BlogImageMapper, BlogImage> implements BlogImageService {
}
